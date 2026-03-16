package org.molgenis.vcf.utils.model.metadata;

import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.jspecify.annotations.Nullable;
import org.molgenis.vcf.utils.metadata.NestedAttributes;
import org.molgenis.vcf.utils.metadata.ValueCount;
import org.molgenis.vcf.utils.metadata.ValueType;
import org.molgenis.vcf.utils.model.ValueDescription;

@Getter
@SuperBuilder
@EqualsAndHashCode
@ToString
public class FieldMetadata {
  ValueType type;
  ValueCount.Type numberType;
  @Nullable Integer numberCount;
  @Builder.Default boolean required = false;
  @Nullable Character separator;
  @Nullable Map<String, ValueDescription> categories;
  String label;
  @Nullable String description;
  @Nullable Map<String, NestedFieldMetadata> nestedFields;
  @Nullable NestedAttributes nestedAttributes;
  @Nullable ValueDescription nullValue;
}
