package org.molgenis.vcf.utils;

import static java.lang.String.format;

import java.io.Serial;

public class InvalidSamplePhenotypesException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;
  private final String argument;

  public InvalidSamplePhenotypesException(String argument) {
    this.argument = argument;
  }

  @Override
  public String getMessage() {
    return format(
        "Invalid phenotype argument: '%s', valid example: 'sample1/phenotype1;phenotype2,sample2/phenotype1'",
        argument);
  }
}
