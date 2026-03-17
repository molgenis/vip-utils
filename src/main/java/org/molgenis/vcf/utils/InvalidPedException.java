package org.molgenis.vcf.utils;

import static java.lang.String.format;

import java.io.Serial;

public class InvalidPedException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;
  private final String argument;

  public InvalidPedException(String argument) {
    this.argument = argument;
  }

  @Override
  public String getMessage() {
    return format("Invalid PED line, expected 6 columns on line: %s", argument);
  }
}
