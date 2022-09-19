package org.molgenis.vcf.utils.model;

public enum FieldType {
  COMMON,
  INFO,
  /**
   * INFO field with nested information (VEP CSQ)
   */
  INFO_VEP,
  FORMAT,
}
