package org.molgenis.vcf.utils.vep;

import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.molgenis.vcf.utils.metadata.FieldMetadataService;
import org.molgenis.vcf.utils.model.Field;
import org.molgenis.vcf.utils.model.NestedField;
import org.molgenis.vcf.utils.model.FieldMetadata;
import org.molgenis.vcf.utils.model.NumberType;
import org.molgenis.vcf.utils.model.ValueType;
import org.springframework.stereotype.Component;

@Component
public class VepMetadataService implements FieldMetadataService {

  private final FieldMetadataService fieldMetadataService;

  private static final String INFO_DESCRIPTION_PREFIX = "Consequence annotations from Ensembl VEP. Format: ";

  public VepMetadataService(
      FieldMetadataService fieldMetadataService) {
    this.fieldMetadataService = fieldMetadataService;
  }

  @Override
  public FieldMetadata load(VCFInfoHeaderLine vcfInfoHeaderLine) {
    FieldMetadata fieldMetadata = fieldMetadataService.load(vcfInfoHeaderLine);
    return postProcess(fieldMetadata, vcfInfoHeaderLine);
  }

  private FieldMetadata postProcess(FieldMetadata fieldMetadata,
      VCFInfoHeaderLine vcfInfoHeaderLine) {
    Map<String, NestedField> postProcessedFields = new HashMap<>();

    Field parent = Field.builder().label(vcfInfoHeaderLine.getID()).description(vcfInfoHeaderLine.getDescription())
        .type(ValueType.STRING).numberType(NumberType.OTHER)
        .separator('|').build();

    Map<String, Integer> nestedFieldIndices = getNestedInfoIds(vcfInfoHeaderLine);
    for (Entry<String, NestedField> entry : fieldMetadata.getNestedFields().entrySet()) {
      NestedField field = entry.getValue();
      Integer index = nestedFieldIndices.get(entry.getKey());
      if (index != null) {
        postProcessedFields.put(entry.getKey(),
            NestedField.builder().numberType(field.getNumberType()).separator(field.getSeparator())
                .categories(field.getCategories()).index(index).numberCount(field.getNumberCount())
                .type(field.getType()).label(field.getLabel()).description(field.getDescription())
                .required(field.isRequired()).build());
      }
    }

    return FieldMetadata.builder().field(parent)
        .nestedFields(addUnmappedFields(postProcessedFields, nestedFieldIndices)).build();
  }

  private Map<String, NestedField> addUnmappedFields(Map<String, NestedField> metadataFields, Map<String, Integer> nestedFieldIndices) {
    nestedFieldIndices.entrySet().stream()
        .filter(entry -> !metadataFields.containsKey(entry.getKey())).forEach(
            entry -> metadataFields.put(entry.getKey(),
                NestedField.builder().label(entry.getKey()).description(entry.getKey())
                    .numberType(NumberType.NUMBER).index(entry.getValue()).numberCount(1)
                    .type(ValueType.STRING).required(false).build()));
    return metadataFields;
  }


  private Map<String, Integer> getNestedInfoIds(VCFInfoHeaderLine vcfInfoHeaderLine) {
    String description = vcfInfoHeaderLine.getDescription();
    String[] infoIds = description.substring(INFO_DESCRIPTION_PREFIX.length()).split("\\|", -1);
    int index = 0;
    Map<String, Integer> nestedFieldIndices = new HashMap<>();
    for (String id : infoIds) {
      nestedFieldIndices.put(id, index);
      index++;
    }
    return nestedFieldIndices;
  }
}
