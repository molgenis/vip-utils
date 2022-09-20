package org.molgenis.vcf.utils.model;

import java.util.Set;

public interface Field {

  String getId();

  FieldType getFieldType();

  ValueType getType();

  NumberType getNumberType();

  Integer getNumberCount();

  Character getSeparator();

  Set<String> getCategories();

  boolean isRequired();

  String getLabel();

  String getDescription();
}
