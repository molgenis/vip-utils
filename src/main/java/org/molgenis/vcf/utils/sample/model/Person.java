package org.molgenis.vcf.utils.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
@AllArgsConstructor
@NonFinal
public class Person {
  @JsonProperty("familyId")
  String familyId;

  @JsonProperty("individualId")
  String individualId;

  @JsonProperty("paternalId")
  String paternalId;

  @JsonProperty("maternalId")
  String maternalId;

  @JsonProperty("sex")
  Sex sex;

  @JsonProperty("affectedStatus")
  AffectedStatus affectedStatus;
}
