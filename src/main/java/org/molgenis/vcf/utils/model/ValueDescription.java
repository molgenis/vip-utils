package org.molgenis.vcf.utils.model;

import lombok.*;

@Value
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ValueDescription {
    @NonNull String label;
    String description;
}
