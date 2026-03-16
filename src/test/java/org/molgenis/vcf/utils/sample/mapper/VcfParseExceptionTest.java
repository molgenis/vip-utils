package org.molgenis.vcf.utils.sample.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VcfParseExceptionTest {

  @Test
  void getMessage() {
    assertEquals(
        "error parsing vcf file: MyMessage", new VcfParseException("MyMessage").getMessage());
  }
}
