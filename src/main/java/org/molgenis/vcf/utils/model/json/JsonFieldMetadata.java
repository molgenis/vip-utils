package org.molgenis.vcf.utils.model.json;

import java.util.Map;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.jspecify.annotations.Nullable;
import org.molgenis.vcf.utils.metadata.NestedAttributes;
import org.molgenis.vcf.utils.metadata.ValueType;
import org.molgenis.vcf.utils.model.NumberType;
import org.molgenis.vcf.utils.model.ValueDescription;

@Getter
@SuperBuilder
@Jacksonized
public class JsonFieldMetadata {
  ValueType type;
  NumberType numberType;
  @Nullable Integer numberCount;
  @Nullable Boolean required;
  @Nullable Character separator;
  @Nullable Map<String, ValueDescription> categories;
  String label;
  @Nullable String description;
  @Nullable Map<String, NestedJsonFieldMetadata> nestedFields;
  @Nullable ValueDescription nullValue;
  @Nullable NestedAttributes nestedAttributes;
}
