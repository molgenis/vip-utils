package org.molgenis.vcf.utils.metadata;

import static java.lang.String.format;

import java.io.Serial;

public class NestedMetadataWithoutIndexException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public NestedMetadataWithoutIndexException(String key) {
    super(
        format(
            "Nested metadata '%s' does not have an index, while it's parent does not have a specified prefix.",
            key));
  }
}
