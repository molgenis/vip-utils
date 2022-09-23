package org.molgenis.vcf.utils.metadata;


import org.springframework.lang.NonNull;

public class UnknownFieldException extends RuntimeException {

  private static final String MESSAGE = "No known nested metadata for identifier '%s'.";
  private final String id;

  public UnknownFieldException(@NonNull String id) {
    this.id = id;
  }

  @Override
  public String getMessage(){
    return String.format(MESSAGE, id);
  }
}
