package org.molgenis.vcf.utils.sample.mapper;

import lombok.Value;
import org.jspecify.annotations.Nullable;

@Value
public class SamplePhenotype {

  PhenotypeMode mode;

  @Nullable String subjectId;

  String[] phenotypes;
}
