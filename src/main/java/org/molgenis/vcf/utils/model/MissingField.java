package org.molgenis.vcf.utils.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class MissingField implements Field {

  @NonNull String id;

  @Override
  public FieldType getFieldType() {
    throw new UnsupportedOperationException(
        String
            .format("Fieldtype is unavailable for field '%s' that is not present in the input vcf.",
                id));
  }

  @Override
  public ValueType getType() {
    throw new UnsupportedOperationException(
        String
            .format("Type is unavailable for field '%s' that is not present in the input vcf.",
                id));
  }

  @Override
  public NumberType getNumberType() {
    throw new UnsupportedOperationException(
        String.format(
            "NumberType is unavailable for field '%s' that is not present in the input vcf.", id));
  }

  @Override
  public Integer getNumberCount() {
    throw new UnsupportedOperationException(
        String.format("Count is unavailable for field '%s' that is not present in the input vcf.",
            id));
  }

  @Override
  public Character getSeparator() {
    throw new UnsupportedOperationException(
        String
            .format("Separator is unavailable for field '%s' that is not present in the input vcf.",
                id));
  }

  @Override
  public Set<String> getCategories() {
    throw new UnsupportedOperationException(
        String
            .format("Categories are unavailable for field '%s' that is not present in the input vcf.",
                id));
  }

  @Override
  public boolean isRequired() {
    throw new UnsupportedOperationException(
        String
            .format("Required is unavailable for field '%s' that is not present in the input vcf.",
                id));
  }

  @Override
  public String getLabel() {
    throw new UnsupportedOperationException(
        String
            .format("Label is unavailable for field '%s' that is not present in the input vcf.",
                id));
  }

  @Override
  public String getDescription() {
    throw new UnsupportedOperationException(
        String
            .format("Description is unavailable for field '%s' that is not present in the input vcf.",
                id));
  }
}