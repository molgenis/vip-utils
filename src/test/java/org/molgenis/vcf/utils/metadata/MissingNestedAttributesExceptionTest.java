package org.molgenis.vcf.utils.metadata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MissingNestedAttributesExceptionTest {
    @Test
    void getMessage() {
        assertEquals(
                "Parent metadata 'test' does not have 'NestedAttributes' (separator and optional prefix), while it has nested fields.",
                new MissingNestedAttributesException("test").getMessage());
    }
}