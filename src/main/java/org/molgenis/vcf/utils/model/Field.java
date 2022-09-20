package org.molgenis.vcf.utils.model;

import java.util.Set;
import org.springframework.lang.NonNull;

public interface Field {

  @NonNull String getId();

  @NonNull FieldType getFieldType();

  @NonNull ValueType getType();

  @NonNull NumberType getNumberType();

  Integer getNumberCount();

  Character getSeparator();

  Set<String> getCategories();

  boolean isRequired();

  @NonNull String getLabel();

  @NonNull String getDescription();
}
