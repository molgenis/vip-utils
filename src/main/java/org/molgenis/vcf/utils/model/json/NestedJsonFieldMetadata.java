package org.molgenis.vcf.utils.model.json;

import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;
import org.molgenis.vcf.utils.model.metadata.NestedFieldMetadata;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.molgenis.vcf.utils.metadata.FieldMetadataServiceImpl.mapNumberType;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class NestedJsonFieldMetadata extends JsonFieldMetadata {
    @Default
    int index = -1;

    public NestedFieldMetadata toFieldNestedFieldMetadata() {
        return NestedFieldMetadata.builder()
                .nestedFields(nestedFields != null ? nestedFields.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().toFieldNestedFieldMetadata()
                        )) : Collections.emptyMap())
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
