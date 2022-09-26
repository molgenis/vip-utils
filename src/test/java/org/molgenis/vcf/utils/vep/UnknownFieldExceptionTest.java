package org.molgenis.vcf.utils.vep;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.molgenis.vcf.utils.metadata.UnknownFieldException;

class UnknownFieldExceptionTest {

  @Test
  void getMessage() {
    assertEquals("No known nested metadata for identifier 'TEST'.",new UnknownFieldException("TEST").getMessage());
  }
}