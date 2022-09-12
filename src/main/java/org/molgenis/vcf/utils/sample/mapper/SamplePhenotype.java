package org.molgenis.vcf.utils.sample.mapper;

import lombok.NonNull;
import lombok.Value;

@Value
public class SamplePhenotype {

  @NonNull PhenotypeMode mode;

  String subjectId;

  @NonNull String[] phenotypes;
}
