package org.molgenis.vcf.utils.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Phenopacket {
  @JsonProperty("phenotypicFeaturesList")
  List<PhenotypicFeature> phenotypicFeaturesList;

  @JsonProperty("subject")
  Individual subject;
}
