package org.molgenis.vcf.utils.model.metadata;

import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.NonFinal;

import java.util.Collections;
import java.util.Map;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NonFinal
public class FieldMetadatas {
  @NonNull @Default Map<String, FieldMetadata> info = Collections.emptyMap();
  @NonNull @Default Map<String, FieldMetadata> format = Collections.emptyMap();
}
