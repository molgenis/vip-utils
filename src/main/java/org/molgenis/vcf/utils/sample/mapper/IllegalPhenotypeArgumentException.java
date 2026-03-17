package org.molgenis.vcf.utils.sample.mapper;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.io.Serial;

public class IllegalPhenotypeArgumentException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;
  private final String argument;

  public IllegalPhenotypeArgumentException(String argument) {
    this.argument = requireNonNull(argument);
  }

  @Override
  public String getMessage() {
    return format(
        "Illegal phenotype '%s' phenotypes must be specified in CURIE (prefix:reference) format.",
        argument);
  }
}
