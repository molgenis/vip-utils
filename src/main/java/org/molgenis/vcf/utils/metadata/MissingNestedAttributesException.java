package org.molgenis.vcf.utils.metadata;

import static java.lang.String.format;

import java.io.Serial;

public class MissingNestedAttributesException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public MissingNestedAttributesException(String name) {
    super(
        format(
            "Parent metadata '%s' does not have 'NestedAttributes' (separator and optional prefix), while it has nested fields.",
            name));
  }
}
