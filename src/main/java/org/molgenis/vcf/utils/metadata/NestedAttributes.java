package org.molgenis.vcf.utils.metadata;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class NestedAttributes {
    @NonNull String prefix;
    @NonNull String seperator;
}
