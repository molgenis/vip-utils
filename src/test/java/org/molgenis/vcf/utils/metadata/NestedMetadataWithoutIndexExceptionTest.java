package org.molgenis.vcf.utils.metadata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NestedMetadataWithoutIndexExceptionTest {
    @Test
    void getMessage() {
        assertEquals(
                "Nested metadata 'test' does not have an index, while it's parent does not have a specified prefix.",
                new NestedMetadataWithoutIndexException("test").getMessage());
    }
}