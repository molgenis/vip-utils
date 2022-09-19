package org.molgenis.vcf.utils.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MissingFieldTest {

  MissingField field = new MissingField("TEST");

  @Test
  void getId() {
    assertEquals("TEST", field.getId());
  }

  @Test
  void getFieldType() {
    assertThrows(UnsupportedOperationException.class, () -> field.getFieldType());
  }

  @Test
  void getType() {
    assertThrows(UnsupportedOperationException.class, () -> field.getType());
  }

  @Test
  void getNumberType() {
    assertThrows(UnsupportedOperationException.class, () -> field.getNumberType());
  }

  @Test
  void getNumberCount() {
    assertThrows(UnsupportedOperationException.class, () -> field.getNumberCount());
  }

  @Test
  void getSeparator() {
    assertThrows(UnsupportedOperationException.class, () -> field.getSeparator());
  }

  @Test
  void getCategories() {
    assertThrows(UnsupportedOperationException.class, () -> field.getCategories());
  }

  @Test
  void isRequired() {
    assertThrows(UnsupportedOperationException.class, () -> field.isRequired());
  }

  @Test
  void getLabel() {
    assertThrows(UnsupportedOperationException.class, () -> field.getLabel());
  }

  @Test
  void getDescription() {
    assertThrows(UnsupportedOperationException.class, () -> field.getDescription());
  }
}