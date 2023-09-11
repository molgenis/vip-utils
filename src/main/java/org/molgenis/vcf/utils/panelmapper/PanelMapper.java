package org.molgenis.vcf.utils.panelmapper;

import ch.qos.logback.classic.Level;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.*;
import org.molgenis.vcf.utils.panelmapper.model.GeneLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;
import static org.molgenis.vcf.utils.panelmapper.GeneConverter.convertSymbolsToGeneIds;
import static org.molgenis.vcf.utils.panelmapper.GeneFileReader.readGenesFile;

class PanelMapper{

  private static final Logger LOGGER = LoggerFactory.getLogger(PanelMapper.class);

  static final String OPT_INPUT = "i";
  static final String OPT_INPUT_LONG = "input";
  static final String OPT_GENES_INPUT = "g";
  static final String OPT_GENES_INPUT_LONG = "genes";
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

    LOGGER.info("Start converting file.");
    try {
      Path inputPath = Path.of(commandLine.getOptionValue(OPT_INPUT));
      Path genesPath = Path.of(commandLine.getOptionValue(OPT_GENES_INPUT));
      Path outputPath = getOutput(commandLine);

      List<GeneLine> geneLines = readGenesFile(genesPath);
      List<String> panel = convertSymbolsToGeneIds(inputPath, geneLines);
      writeToFile(outputPath, panel);

    } catch (Exception e) {
      LOGGER.error(e.getLocalizedMessage(), e);
      System.exit(STATUS_MISC_ERROR);
    }
    LOGGER.info("Done converting file.");
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
            .replace(".txt", "_mapped.tsv"));
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
    String cmdLineSyntax = "java -jar PanelMapper.jar";
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
                    .desc("Input panel file.")
                    .build());
    appOptions.addOption(
            Option.builder(OPT_GENES_INPUT)
                    .hasArg(true)
                    .required()
                    .longOpt(OPT_GENES_INPUT_LONG)
                    .desc("Input Biomart genes file (see README for details).")
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
    if (commandLine.hasOption(OPT_GENES_INPUT)) {
      Path inputPath = Path.of(commandLine.getOptionValue(OPT_GENES_INPUT));
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
      if (!inputPathStr.endsWith(".txt")) {
        throw new IllegalArgumentException(
                format("Input file '%s' is not a %s file.", inputPathStr, ".txt"));
      }
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

  private static void writeToFile(
          Path output, Collection<String> genes) {
    try {
      Files.write(output, genes, Charset.defaultCharset());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
