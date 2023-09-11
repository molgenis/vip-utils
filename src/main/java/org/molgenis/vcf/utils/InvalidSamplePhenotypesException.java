package org.molgenis.vcf.utils;

import java.io.Serial;

import static java.lang.String.format;

public class InvalidSamplePhenotypesException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;
  private static final String MESSAGE =
      "Invalid phenotype argument: '%s', valid example: 'sample1/phenotype1;phenotype2,sample2/phenotype1'";
  private final String argument;

  public InvalidSamplePhenotypesException(String argument) {
    this.argument = argument;
  }

  @Override
  public String getMessage() {
    return format(MESSAGE, argument);
  }
}
