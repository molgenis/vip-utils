package org.molgenis.vcf.utils.metadata;

import lombok.NonNull;

import static java.lang.String.format;

public class MissingNestedAttributesException extends RuntimeException {
    public MissingNestedAttributesException(@NonNull String name) {
        super(
                format(
                        "Parent metadata '%s' does not have 'NestedAttributes' (separator and optional prefix), while it has nested fields.", name));
    }
}