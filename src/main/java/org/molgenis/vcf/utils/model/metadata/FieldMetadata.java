package org.molgenis.vcf.utils.model.metadata;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.molgenis.vcf.utils.metadata.ValueCount;
import org.molgenis.vcf.utils.metadata.ValueType;
import org.molgenis.vcf.utils.model.ValueDescription;

import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
public class FieldMetadata {
  @NonNull
  ValueType type;
  @NonNull
  ValueCount.Type numberType;
  Integer numberCount;
  @Builder.Default boolean required = false;
  Character separator;
  Map<String, ValueDescription> categories;
  @NonNull String label;
  String description;
  Map<String, NestedFieldMetadata> nestedFields;
  ValueDescription nullValue;
}
