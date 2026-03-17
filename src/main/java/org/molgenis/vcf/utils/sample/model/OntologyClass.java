package org.molgenis.vcf.utils.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class OntologyClass {
  @JsonProperty("id")
  String id;

  @JsonProperty("label")
  String label;
}
