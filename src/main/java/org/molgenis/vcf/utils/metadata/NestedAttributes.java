package org.molgenis.vcf.utils.metadata;

import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class NestedAttributes {
    String prefix;
    @NonNull String separator;
}
