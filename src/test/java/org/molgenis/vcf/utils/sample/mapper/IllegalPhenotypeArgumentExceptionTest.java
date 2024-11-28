package org.molgenis.vcf.utils.sample.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IllegalPhenotypeArgumentExceptionTest {
    @Test
    void getMessage() {
        assertEquals(
                "Illegal phenotype 'test' phenotypes must be specified in CURIE (prefix:reference) format.",
                new IllegalPhenotypeArgumentException("test").getMessage());
    }
}
