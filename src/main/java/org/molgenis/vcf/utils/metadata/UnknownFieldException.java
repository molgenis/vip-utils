package org.molgenis.vcf.utils.metadata;

import java.io.Serial;

public class UnknownFieldException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;
  private final String id;

  public UnknownFieldException(String id) {
    this.id = id;
  }

  @Override
  public String getMessage() {
    return String.format("No known nested metadata for identifier '%s'.", id);
  }
}
