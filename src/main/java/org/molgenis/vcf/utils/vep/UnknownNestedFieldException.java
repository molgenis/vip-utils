package org.molgenis.vcf.utils.vep;


import org.springframework.lang.NonNull;

public class UnknownNestedFieldException extends RuntimeException {

  private static final String MESSAGE = "No known nested metadata for identifier '%s'.";
  private final String id;

  public UnknownNestedFieldException(@NonNull String id) {
    this.id = id;
  }

  @Override
  public String getMessage(){
    return String.format(MESSAGE, id);
  }
}
