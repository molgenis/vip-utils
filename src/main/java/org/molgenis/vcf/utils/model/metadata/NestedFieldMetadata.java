package org.molgenis.vcf.utils.model.metadata;

import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NestedFieldMetadata extends FieldMetadata {
  @Default int index = -1;
}
