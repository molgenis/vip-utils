package org.molgenis.vcf.report.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.molgenis.vcf.utils.sample.mapper.VcfParseException;

class VcfParseExceptionTest {

  @Test
  void getMessage() {
    assertEquals(
        "error parsing vcf file: MyMessage", new VcfParseException("MyMessage").getMessage());
  }
}
