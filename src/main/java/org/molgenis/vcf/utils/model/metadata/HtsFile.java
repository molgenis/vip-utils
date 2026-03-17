package org.molgenis.vcf.utils.model.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class HtsFile {

  @JsonProperty("uri")
  String uri;

  @JsonProperty("htsFormat")
  HtsFormat htsFormat;

  @JsonProperty("genomeAssembly")
  String genomeAssembly;
}
