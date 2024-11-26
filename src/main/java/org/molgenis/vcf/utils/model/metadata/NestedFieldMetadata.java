package org.molgenis.vcf.utils.model.metadata;

import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
public class NestedFieldMetadata extends FieldMetadata {
  @Default int index = -1;
}
