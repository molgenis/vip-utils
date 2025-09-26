package org.molgenis.vcf.utils.metadata;

import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class NestedAttributes {
    @NonNull String prefix;
    @NonNull String separator;
}
