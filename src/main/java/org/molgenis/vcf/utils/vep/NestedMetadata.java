package org.molgenis.vcf.utils.vep;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.molgenis.vcf.utils.model.Field;
import org.molgenis.vcf.utils.model.NestedField;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NonFinal
public class NestedMetadata {
  Field parent;
  Map<String, NestedField> vepMetadata;
}
