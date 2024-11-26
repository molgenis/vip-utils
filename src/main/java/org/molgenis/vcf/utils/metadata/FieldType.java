package org.molgenis.vcf.utils.metadata;

public enum FieldType {
  COMMON,
  INFO,
  /**
   * INFO field with nested information
   */
  INFO_NESTED,
  FORMAT,
  /**
   * FORMAT field with nested information (GENOTYPE info from htsjdk)
   *
   */
  GENOTYPE,
  /**
   * Sample information
   *
   */
  SAMPLE
}
