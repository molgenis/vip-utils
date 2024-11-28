package org.molgenis.vcf.utils.hpo.model.input;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class Hpo {
    @NonNull
    List<HpoGraph> graphs;
}
