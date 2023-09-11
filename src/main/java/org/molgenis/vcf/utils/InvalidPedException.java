package org.molgenis.vcf.utils;

import java.io.Serial;

import static java.lang.String.format;

public class InvalidPedException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;
  private static final String MESSAGE = "Invalid PED line, expected 6 columns on line: %s";
  private final String argument;

  public InvalidPedException(String argument) {
    this.argument = argument;
  }

  @Override
  public String getMessage() {
    return format(MESSAGE, argument);
  }
}
