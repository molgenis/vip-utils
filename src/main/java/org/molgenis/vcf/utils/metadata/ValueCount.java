package org.molgenis.vcf.utils.metadata;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
@NonFinal
public class ValueCount {

  public enum Type {
    A,
    R,
    G,
    VARIABLE,
    FIXED
  }

  Type type;

  /** Returns count for FIXED values, otherwise null */
  Integer count;

  /** True if null value (list item) is allowed */
  boolean nullable;
}
