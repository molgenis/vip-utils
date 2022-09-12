package org.molgenis.vcf.utils.sample.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pedigree {

  String id;
  Map<String, Sample> members;
}
