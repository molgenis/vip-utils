package org.molgenis.vcf.utils.metadata;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MissingNestedAttributesExceptionTest {
  @Test
  void getMessage() {
    assertEquals(
        "Parent metadata 'test' does not have 'NestedAttributes' (separator and optional prefix), while it has nested fields.",
        new MissingNestedAttributesException("test").getMessage());
  }
}
