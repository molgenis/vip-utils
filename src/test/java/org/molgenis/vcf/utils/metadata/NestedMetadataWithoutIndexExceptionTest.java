package org.molgenis.vcf.utils.metadata;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NestedMetadataWithoutIndexExceptionTest {
  @Test
  void getMessage() {
    assertEquals(
        "Nested metadata 'test' does not have an index, while it's parent does not have a specified prefix.",
        new NestedMetadataWithoutIndexException("test").getMessage());
  }
}
