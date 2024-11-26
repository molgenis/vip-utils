package org.molgenis.vcf.utils.hpo.model.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class HpoGraphNodeMeta {
    HpoGraphNodeMetaDefinition definition;
}
