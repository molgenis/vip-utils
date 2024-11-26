package org.molgenis.vcf.utils.metadata;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class FieldIdentifier {
    @NonNull FieldType type;
    @NonNull String name;
}
