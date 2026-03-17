package org.molgenis.vcf.utils.metadata;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MissingSeparatorExceptionTest {
  @Test
  void getMessage() {
    assertEquals(
        "Parent metadata 'test' does not have a 'nestedAttributes' separator.",
        new MissingSeparatorException("test").getMessage());
  }
}
