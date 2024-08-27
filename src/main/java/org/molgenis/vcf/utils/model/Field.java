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
public class Field {
  @NonNull ValueType type;
  @NonNull NumberType numberType;
  Integer numberCount;
  @Default boolean required = false;
  Character separator;
  Set<String> categories;
  @NonNull String label;
  @NonNull String description;
}
