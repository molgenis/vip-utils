package org.molgenis.vcf.utils.sample.mapper;

import java.io.Serial;

public class VcfParseException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;
  public VcfParseException(String message) {
    super(message);
  }

  @Override
  public String getMessage() {
    return "error parsing vcf file: " + super.getMessage();
  }
}
