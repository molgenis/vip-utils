package org.molgenis.vcf.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MixedPhenotypesExceptionTest {
    @Test
    void getMessage() {
        assertEquals(
                "Mixing general phenotypes for all samples and phenotypes per sample is not allowed.",
                new MixedPhenotypesException().getMessage());
    }
}
