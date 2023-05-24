package org.molgenis.vcf.utils.panelmapper;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.molgenis.vcf.utils.panelmapper.model.GeneLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneConverter {

  private static final Logger LOGGER = LoggerFactory.getLogger(GeneConverter.class);

  private GeneConverter() {
  }

  public static void run(Path inputPath, Path genesPath, Path output) {
    List<GeneLine> geneLines = readGenesFile(genesPath);
    Map<String, String> mapping =
        convertToGeneID(geneLines);
    List<String> panel = mapPanel(inputPath, mapping);
    writeToFile(output, panel);
  }

  private static List<String> mapPanel(Path inputPath, Map<String, String> mapping) {
    List<String> result;
    try (Stream<String> lines = Files.lines(inputPath)) {
      result = lines.map(symbol -> mapSymbol(symbol, mapping))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return result;
  }

  private static String mapSymbol(String symbol,
      Map<String, String> mapping) {
    if (mapping.containsKey(symbol.toUpperCase())) {
      return String.format("%s", mapping.get(symbol.toUpperCase()));
    } else {
      throw new IllegalArgumentException(String.format("Unknown gene symbol: %s", symbol));
    }
  }

  private static Map<String, String> convertToGeneID(List<GeneLine> geneLines) {
    Map<String, String> geneMapping = new HashMap<>();
    for (GeneLine geneLine : geneLines) {
      String ncbiId = geneLine.getNcbi();
      if (ncbiId != null && !ncbiId.isEmpty()) {
        if (!geneMapping.containsKey(geneLine.getApproved())) {
          geneMapping.put(geneLine.getApproved().toUpperCase(), ncbiId);
        }
        if (!geneLine.getAlias().isEmpty()) {
          geneMapping.put(geneLine.getAlias().toUpperCase(), ncbiId);
        }
        if (!geneLine.getPrevious().isEmpty()) {
          geneMapping.put(geneLine.getPrevious().toUpperCase(), ncbiId);
        }
      }
    }
    return geneMapping;
  }

  private static List<GeneLine> readGenesFile(Path genesPath) {
    List<GeneLine> genesLines;
    try (InputStream fileStream = new FileInputStream(genesPath.toFile());
        Reader reader = new BufferedReader(new InputStreamReader(fileStream))) {
      CsvToBean<GeneLine> csvToBean =
          new CsvToBeanBuilder<GeneLine>(reader)
              .withSeparator('\t')
              .withType(GeneLine.class)
              .withThrowExceptions(false)
              .build();
      genesLines = csvToBean.parse();
      handleCsvParseExceptions(csvToBean.getCapturedExceptions());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return genesLines;
  }

  static void handleCsvParseExceptions(List<CsvException> exceptions) {
    exceptions.forEach(csvException -> LOGGER
        .error(String.format("%s,%s", csvException.getLineNumber(), csvException.getMessage())));
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
