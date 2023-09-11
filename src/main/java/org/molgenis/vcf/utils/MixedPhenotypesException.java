package org.molgenis.vcf.utils;

import java.io.Serial;

public class MixedPhenotypesException extends IllegalArgumentException {
  @Serial
  private static final long serialVersionUID = 1L;
  private static final String MESSAGE =
      "Mixing general phenotypes for all samples and phenotypes per sample is not allowed.";

  public MixedPhenotypesException() {
    super(MESSAGE);
  }
}
