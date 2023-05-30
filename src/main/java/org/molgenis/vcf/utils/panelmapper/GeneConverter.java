package org.molgenis.vcf.utils.panelmapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.molgenis.vcf.utils.panelmapper.model.GeneLine;

public class GeneConverter {
  private GeneConverter() {
  }

  static List<String> convertSymbolsToGeneIds(Path inputPath, List<GeneLine> geneLines) {
    List<String> result;
    Map<String, String> mapping =
            createMapping(geneLines);
    try (Stream<String> lines = Files.lines(inputPath)) {
      result = lines.map(symbol -> convertSymbolToGeneId(symbol, mapping)).toList();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return result;
  }

  private static String convertSymbolToGeneId(String symbol,
      Map<String, String> mapping) {
    if (mapping.containsKey(symbol.toUpperCase())) {
      return String.format("%s", mapping.get(symbol.toUpperCase()));
    } else {
      throw new IllegalArgumentException(String.format("Unknown gene symbol: %s", symbol));
    }
  }

  static Map<String, String> createMapping(List<GeneLine> geneLines) {
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
}