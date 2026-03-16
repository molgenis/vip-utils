package org.molgenis.vcf.utils.hpo.model.input;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Hpo {

  List<HpoGraph> graphs;
}
