package org.molgenis.vcf.utils;

import lombok.Value;

@Value
public class PedIndividual {
  public enum Sex {
    MALE,
    FEMALE,
    UNKNOWN
  }

  public enum AffectionStatus {
    AFFECTED,
    UNAFFECTED,
    UNKNOWN
  }

  String familyId;

  String id;

  String paternalId;

  String maternalId;

  Sex sex;

  PedIndividual.AffectionStatus affectionStatus;
}
