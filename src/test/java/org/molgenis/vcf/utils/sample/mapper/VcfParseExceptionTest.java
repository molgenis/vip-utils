package org.molgenis.vcf.utils.sample.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VcfParseExceptionTest {

    @Test
    void getMessage() {
        assertEquals(
                "error parsing vcf file: MyMessage", new VcfParseException("MyMessage").getMessage());
    }
}
