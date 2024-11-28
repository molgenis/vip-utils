package org.molgenis.vcf.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidSamplePhenotypesExceptionTest {
    @Test
    void getMessage() {
        assertEquals(
                "Invalid phenotype argument: 'test', valid example: 'sample1/phenotype1;phenotype2,sample2/phenotype1'",
                new InvalidSamplePhenotypesException("test").getMessage());
    }
}
