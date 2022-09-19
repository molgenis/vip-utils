package org.molgenis.vcf.utils.model;

import java.util.Set;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
public class FieldImpl implements Field {
  @NonNull
  public enum NumberType {
    NUMBER,
    PER_ALT,
    PER_ALT_AND_REF,
    PER_GENOTYPE,
    OTHER
  }

  @NonNull String id;
  @NonNull FieldType fieldType;
  @NonNull ValueType type;
  @NonNull NumberType numberType;
  Integer numberCount;
  @Default boolean required = false;
  Character separator;
  Set<String> categories;
}
