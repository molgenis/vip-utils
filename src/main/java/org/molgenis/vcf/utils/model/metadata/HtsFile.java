package org.molgenis.vcf.utils.model.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class HtsFile {

  @JsonProperty("uri")
  @NonNull
  String uri;

  @JsonProperty("htsFormat")
  @NonNull
  HtsFormat htsFormat;

  @JsonProperty("genomeAssembly")
  @NonNull
  String genomeAssembly;
}
