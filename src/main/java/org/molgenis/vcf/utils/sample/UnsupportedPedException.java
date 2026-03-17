package org.molgenis.vcf.utils.sample;

import static java.lang.String.format;

import java.io.Serial;

public class UnsupportedPedException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;
  private final String token;

  public UnsupportedPedException(String token) {
    this.token = token;
  }

  @Override
  public String getMessage() {
    return format(
        "Phenotype value '%s' that is not an affection status (-9, 0, 1 or 2) is unsupported",
        token);
  }
}
