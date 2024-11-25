package org.molgenis.vcf.utils.metadata;

public class MissingPrefixException extends RuntimeException {
    private final String field;

    public MissingPrefixException(String id) {
        this.field = id;
    }

    @Override
    public String getMessage() {
        return String.format("Missing prefix for nested field: %s", field);
    }
}
