package org.molgenis.vcf.utils.metadata;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FieldIdentifier {
  FieldType type;
  String name;
}
