package org.molgenis.vcf.utils.model.json;

import java.util.Map;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.molgenis.vcf.utils.metadata.ValueType;
import org.molgenis.vcf.utils.model.NumberType;
import org.molgenis.vcf.utils.model.ValueDescription;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
public class JsonFieldMetadata {
  ValueType type;
  NumberType numberType;
  Integer numberCount;
  Boolean required;
  Character separator;
  Map<String, ValueDescription> categories;
  String label;
  String description;
  Map<String, NestedJsonFieldMetadata> nestedFields;
  ValueDescription nullValue;
}
