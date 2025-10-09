package org.molgenis.vcf.utils.metadata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MissingSeparatorExceptionTest {
    @Test
    void getMessage() {
        assertEquals(
                "Parent metadata 'test' does not have a 'nestedAttributes' separator.",
                new MissingSeparatorException("test").getMessage());
    }
}