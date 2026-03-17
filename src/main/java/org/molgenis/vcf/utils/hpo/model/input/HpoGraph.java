package org.molgenis.vcf.utils.hpo.model.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class HpoGraph {

  List<HpoGraphNode> nodes;

  List<HpoGraphEdge> edges;
}
