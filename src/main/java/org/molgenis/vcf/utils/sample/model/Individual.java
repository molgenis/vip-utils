package org.molgenis.vcf.utils.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Individual {
  @JsonProperty("id")
  String id;
}
