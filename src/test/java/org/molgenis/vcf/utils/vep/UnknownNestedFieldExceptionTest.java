package org.molgenis.vcf.utils.vep;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UnknownNestedFieldExceptionTest {

  @Test
  void getMessage() {
    assertEquals("No known nested metadata for identifier 'TEST'.",new UnknownNestedFieldException("TEST").getMessage());
  }
}