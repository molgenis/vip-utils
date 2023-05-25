package org.molgenis.vcf.utils;

import org.molgenis.vcf.utils.PedIndividual.AffectionStatus;
import org.molgenis.vcf.utils.PedIndividual.Sex;
import org.molgenis.vcf.utils.sample.UnsupportedPedException;

class PedIndividualParser {
  public PedIndividual parse(String line) {
    String[] tokens = line.split("\\s+");
    if (tokens.length != 6) {
      throw new InvalidPedException(line);
    }

    Sex sex = parseSex(tokens[4]);
    AffectionStatus affectionStatus = parseAffectionStatus(tokens[5]);
    return new PedIndividual(tokens[0], tokens[1], tokens[2], tokens[3], sex, affectionStatus);
  }

  private Sex parseSex(String token) {
    return switch (token) {
      case "1" -> Sex.MALE;
      case "2" -> Sex.FEMALE;
      default -> Sex.UNKNOWN;
    };
  }

  private AffectionStatus parseAffectionStatus(String token) {
    AffectionStatus affectionStatus = switch (token) {
      case "-9", "0" -> AffectionStatus.UNKNOWN;
      case "1" -> AffectionStatus.UNAFFECTED;
      case "2" -> AffectionStatus.AFFECTED;
      default -> throw new UnsupportedPedException(token);
    };
    return affectionStatus;
  }
}
