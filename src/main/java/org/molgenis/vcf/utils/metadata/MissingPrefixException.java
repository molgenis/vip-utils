package org.molgenis.vcf.utils.metadata;

import java.io.Serial;

public class MissingPrefixException extends RuntimeException {
    private final FieldIdentifier field;

    public MissingPrefixException(FieldIdentifier identifier) {
        this.field = identifier;
    }

    @Override
    public String getMessage() {
        return String.format("Missing prefix for nested field: %s/%s", field.getType().name(), field.getName());
    }
}
