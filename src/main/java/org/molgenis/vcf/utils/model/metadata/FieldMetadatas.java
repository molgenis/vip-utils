package org.molgenis.vcf.utils.model.metadata;

import java.util.Collections;
import java.util.Map;
import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.NonFinal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NonFinal
public class FieldMetadatas {
  @Default Map<String, FieldMetadata> info = Collections.emptyMap();
  @Default Map<String, FieldMetadata> format = Collections.emptyMap();
}
