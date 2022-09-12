package org.molgenis.vcf.utils.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class PhenotypicFeature {
  @JsonProperty("type")
  @NonNull
  OntologyClass ontologyClass;
}
