package org.molgenis.vcf.utils.metadata;

import static java.lang.String.format;

public class NestedMetadataWithoutIndexException extends RuntimeException {
    public NestedMetadataWithoutIndexException(String key) {
        super(
                format(
                        "Nested metadata '%s' does not have an index, while it's parent does not have a specified prefix.", key));
    }
}
