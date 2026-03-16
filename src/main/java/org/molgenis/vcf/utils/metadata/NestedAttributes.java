package org.molgenis.vcf.utils.metadata;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class NestedAttributes {
  String prefix;
  String separator;
}
