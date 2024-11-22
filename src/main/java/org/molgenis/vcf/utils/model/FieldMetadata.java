package org.molgenis.vcf.utils.model;

import java.util.Map;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
public class FieldMetadata {
  ValueType type;
  NumberType numberType;
  Integer numberCount;
  @Builder.Default boolean required = false;
  Character separator;
  Map<String, ValueDescription> categories;
  String label;
  String description;
  Map<String, NestedField> nestedFields;
  ValueDescription nullValue;
}
