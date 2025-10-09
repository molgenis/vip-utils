package org.molgenis.vcf.utils.metadata;

import lombok.NonNull;

import static java.lang.String.format;

public class MissingSeparatorException extends RuntimeException {
    public MissingSeparatorException(@NonNull String name) {
        super(
                format(
                        "Parent metadata '%s' does not have a 'nestedAttributes' separator.", name));
    }
}
