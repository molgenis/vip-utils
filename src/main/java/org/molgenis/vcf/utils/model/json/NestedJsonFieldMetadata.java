package org.molgenis.vcf.utils.model.json;

import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
public class NestedJsonFieldMetadata extends JsonFieldMetadata {
  @Default int index = -1;
}
