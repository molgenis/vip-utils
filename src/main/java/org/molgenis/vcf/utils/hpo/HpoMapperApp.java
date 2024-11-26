package org.molgenis.vcf.utils.hpo;

import ch.qos.logback.classic.Level;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

public class HpoMapperApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(HpoMapperApp.class);

    static final String OPT_INPUT = "i";
    static final String OPT_INPUT_LONG = "input";
    static final String OPT_OUTPUT = "o";
    static final String OPT_OUTPUT_LONG = "output";
    static final String OPT_FORCE = "f";
    static final String OPT_FORCE_LONG = "force";
    static final String OPT_DEBUG = "d";
    static final String OPT_DEBUG_LONG = "debug";
    static final String OPT_VERSION = "v";
    static final String OPT_VERSION_LONG = "version";

    private static final int STATUS_MISC_ERROR = 1;
    private static final int STATUS_COMMAND_LINE_USAGE_ERROR = 64;

    public static void main(String[] args) {
        Logger rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        if (!(rootLogger instanceof ch.qos.logback.classic.Logger)) {
            throw new ClassCastException("Expected root logger to be a logback logger");
        }
        ((ch.qos.logback.classic.Logger) rootLogger).setLevel(Level.INFO);


        CommandLine commandLine = getCommandLine(args);
        validateCommandLine(commandLine);

        LOGGER.debug("start converting file...");
        try {
            Path inputPath = Path.of(commandLine.getOptionValue(OPT_INPUT));
            Path outputPath = getOutput(commandLine);

            if (commandLine.hasOption(OPT_FORCE)) {
                Files.deleteIfExists(outputPath);
            }

            try (BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8); BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8, CREATE_NEW)) {
                new HpoMapper().transform(reader, writer);
            }
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            System.exit(STATUS_MISC_ERROR);
        }
        LOGGER.debug("done converting file");
    }

    private static CommandLine getCommandLine(String[] args) {
        CommandLine commandLine = null;
        try {
            CommandLineParser commandLineParser = new DefaultParser();
            commandLine = commandLineParser.parse(getAppOptions(), args);
        } catch (ParseException e) {
            logException(e);
            System.exit(STATUS_COMMAND_LINE_USAGE_ERROR);
        }
        return commandLine;
    }

    private static Path getOutput(CommandLine commandLine) {
        Path outputPath;
        if (commandLine.hasOption(OPT_OUTPUT)) {
            outputPath = Path.of(commandLine.getOptionValue(OPT_OUTPUT));
        } else {
            outputPath = Path.of(commandLine
                    .getOptionValue(OPT_INPUT)
                    .replace(".json", "_mapped.tsv"));
        }
        return outputPath;
    }

    @SuppressWarnings("java:S106")
    private static void logException(ParseException e) {
        LOGGER.error(e.getLocalizedMessage(), e);

        // following information is only logged to system out
        System.out.println();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(null);
        String cmdLineSyntax = "java -jar HpoMapperApp.jar";
        formatter.printHelp(cmdLineSyntax, getAppOptions(), true);
        System.out.println();
        formatter.printHelp(cmdLineSyntax, getAppVersionOptions(), true);
    }

    static Options getAppOptions() {
        Options appOptions = new Options();
        appOptions.addOption(
                Option.builder(OPT_INPUT)
                        .hasArg(true)
                        .longOpt(OPT_INPUT_LONG)
                        .desc("Input file (.json) from https://hpo.jax.org/data/ontology")
                        .required()
                        .build());
        appOptions.addOption(
                Option.builder(OPT_OUTPUT)
                        .hasArg(true)
                        .longOpt(OPT_OUTPUT_LONG)
                        .desc("Output file (.tsv).")
                        .build());
        appOptions.addOption(
                Option.builder(OPT_FORCE)
                        .longOpt(OPT_FORCE_LONG)
                        .desc("Override the output file if it already exists.")
                        .build());
        appOptions.addOption(
                Option.builder(OPT_DEBUG)
                        .longOpt(OPT_DEBUG_LONG)
                        .desc("Enable debug mode (additional logging).")
                        .build());
        return appOptions;
    }

    static Options getAppVersionOptions() {
        Options appVersionOptions = new Options();
        appVersionOptions.addOption(
                Option.builder(OPT_VERSION)
                        .required()
                        .longOpt(OPT_VERSION_LONG)
                        .desc("Print version.")
                        .build());
        return appVersionOptions;
    }

    static void validateCommandLine(CommandLine commandLine) {
        validateInput(commandLine);
        validateOutput(commandLine);
    }

    private static void validateInput(CommandLine commandLine) {
        Path inputPath = Path.of(commandLine.getOptionValue(OPT_INPUT));
        if (!Files.exists(inputPath)) {
            throw new IllegalArgumentException(
                    format("Input file '%s' does not exist.", inputPath));
        }
        if (Files.isDirectory(inputPath)) {
            throw new IllegalArgumentException(
                    format("Input file '%s' is a directory.", inputPath));
        }
        if (!Files.isReadable(inputPath)) {
            throw new IllegalArgumentException(
                    format("Input file '%s' is not readable.", inputPath));
        }
        String inputPathStr = inputPath.toString();
        if (!inputPathStr.endsWith(".json")) {
            throw new IllegalArgumentException(
                    format("Input file '%s' is not a %s file.", inputPathStr, ".json"));
        }
    }

    private static void validateOutput(CommandLine commandLine) {
        if (!commandLine.hasOption(OPT_OUTPUT)) {
            return;
        }

        Path outputPath = Path.of(commandLine.getOptionValue(OPT_OUTPUT));

        if (!commandLine.hasOption(OPT_FORCE) && Files.exists(outputPath)) {
            throw new IllegalArgumentException(
                    format("Output file '%s' already exists", outputPath));
        }
    }
}
