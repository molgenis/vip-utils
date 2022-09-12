package org.molgenis.vcf.utils.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
@AllArgsConstructor
@NonFinal
public class Person {
  @JsonProperty("familyId")
  @NonNull
  String familyId;

  @JsonProperty("individualId")
  @NonNull
  String individualId;

  @JsonProperty("paternalId")
  @NonNull
  String paternalId;

  @JsonProperty("maternalId")
  @NonNull
  String maternalId;

  @JsonProperty("sex")
  @NonNull
  Sex sex;

  @JsonProperty("affectedStatus")
  @NonNull
  AffectedStatus affectedStatus;
}
