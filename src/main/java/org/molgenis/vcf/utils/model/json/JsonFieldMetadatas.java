package org.molgenis.vcf.utils.model.json;

import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NonFinal
public class JsonFieldMetadatas {
  @Default Map<String, JsonFieldMetadata> info = Collections.emptyMap();
  @Default Map<String, JsonFieldMetadata> format = Collections.emptyMap();
}
