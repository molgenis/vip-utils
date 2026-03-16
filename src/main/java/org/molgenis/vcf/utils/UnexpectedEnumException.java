package org.molgenis.vcf.utils;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.io.Serial;

/**
 * Exception that is thrown in the default section of a switch statement as a defensive programming
 * strategy.
 */
public class UnexpectedEnumException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;
  private final Enum<?> enumConstant;

  public <E extends Enum<E>> UnexpectedEnumException(E enumConstant) {
    this.enumConstant = requireNonNull(enumConstant);
  }

  @Override
  public String getMessage() {
    return format(
        "Unexpected enum constant '%s' for type '%s'",
        enumConstant.name(), enumConstant.getDeclaringClass().getSimpleName());
  }
}
