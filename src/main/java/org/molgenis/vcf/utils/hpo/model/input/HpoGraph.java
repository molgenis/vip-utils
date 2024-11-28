package org.molgenis.vcf.utils.hpo.model.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class HpoGraph {
    @NonNull
    List<HpoGraphNode> nodes;
    @NonNull
    List<HpoGraphEdge> edges;
}
