package org.molgenis.vcf.report;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.molgenis.vcf.utils.MixedPhenotypesException;

class MixedPhenotypesExceptionTest {
  @Test
  void getMessage() {
    assertEquals(
        "Mixing general phenotypes for all samples and phenotypes per sample is not allowed.",
        new MixedPhenotypesException().getMessage());
  }
}
