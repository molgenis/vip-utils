package org.molgenis.vcf.utils.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.NonFinal;

@Data
@NonFinal
@Builder
public class Sample {

  @JsonProperty("person")
  @NonNull
  Person person;

  // index of the sample in the VCF, -1 means the sample is not available in the file.
  @JsonProperty("index")
  int index;

  @JsonProperty("proband")
  boolean proband;
}
