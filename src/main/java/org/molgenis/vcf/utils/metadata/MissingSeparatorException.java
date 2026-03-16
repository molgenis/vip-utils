package org.molgenis.vcf.utils.metadata;

import static java.lang.String.format;

import java.io.Serial;

public class MissingSeparatorException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public MissingSeparatorException(String name) {
    super(format("Parent metadata '%s' does not have a 'nestedAttributes' separator.", name));
  }
}
