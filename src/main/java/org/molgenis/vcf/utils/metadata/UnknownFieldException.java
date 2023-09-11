package org.molgenis.vcf.utils.metadata;


import org.springframework.lang.NonNull;

import java.io.Serial;

public class UnknownFieldException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;
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
