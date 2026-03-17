package org.molgenis.vcf.utils.model.json;

import static org.molgenis.vcf.utils.metadata.FieldMetadataServiceImpl.mapNumberType;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.molgenis.vcf.utils.model.metadata.NestedFieldMetadata;

@Getter
@SuperBuilder
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NestedJsonFieldMetadata extends JsonFieldMetadata {
  @Default int index = -1;

  public NestedFieldMetadata toFieldNestedFieldMetadata() {
    return NestedFieldMetadata.builder()
        .nestedFields(
            nestedFields != null
                ? nestedFields.entrySet().stream()
                    .collect(
                        Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().toFieldNestedFieldMetadata()))
                : Collections.emptyMap())
        .index(index)
        .label(label)
        .categories(categories)
        .description(description)
        .nullValue(nullValue)
        .numberCount(numberCount)
        .numberType(mapNumberType(numberType))
        .required(required != null && required)
        .separator(separator)
        .type(type)
        .build();
  }
}
