package org.molgenis.vcf.utils.sample.mapper;

public class VcfParseException extends RuntimeException {

  public VcfParseException(String message) {
    super(message);
  }

  @Override
  public String getMessage() {
    return "error parsing vcf file: " + super.getMessage();
  }
}
