package org.molgenis.vcf.utils.model;

import java.util.Set;
import org.molgenis.vcf.utils.model.FieldImpl.NumberType;

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
