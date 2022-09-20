package org.molgenis.vcf.utils.vep;

import com.fasterxml.jackson.databind.ObjectMapper;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.molgenis.vcf.utils.model.Field;
import org.molgenis.vcf.utils.model.FieldImpl;
import org.molgenis.vcf.utils.model.FieldType;
import org.molgenis.vcf.utils.model.NestedField;
import org.molgenis.vcf.utils.model.NestedMetadata;
import org.molgenis.vcf.utils.model.NestedMetadatas;
import org.molgenis.vcf.utils.model.NumberType;
import org.molgenis.vcf.utils.model.ValueType;
import org.springframework.stereotype.Component;

@Component
public class MetadataServiceImpl implements MetadataService {

  private static final String INFO_DESCRIPTION_PREFIX = "Consequence annotations from Ensembl VEP. Format: ";

  @Override
  public NestedMetadata load(VCFInfoHeaderLine vcfInfoHeaderLine) {
    InputStream in = MetadataServiceImpl.class.getClassLoader()
        .getResourceAsStream("vepMetadata.json");

    return load(in, vcfInfoHeaderLine);
  }

  @Override
  public NestedMetadata load(InputStream in, VCFInfoHeaderLine vcfInfoHeaderLine) {
    ObjectMapper mapper = new ObjectMapper();

    NestedMetadatas nestedMetadatas;
    try {
      nestedMetadatas = mapper.readValue(in, NestedMetadatas.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    if (!nestedMetadatas.getNestedMetadata().containsKey(vcfInfoHeaderLine.getID())) {
      throw new UnknownNestedFieldException(vcfInfoHeaderLine.getID());
    }
    NestedMetadata nestedMetadata = nestedMetadatas.getNestedMetadata().get(vcfInfoHeaderLine.getID());
    return postProcess(nestedMetadata, vcfInfoHeaderLine);
  }

  private NestedMetadata postProcess(NestedMetadata nestedMetadata,
      VCFInfoHeaderLine vcfInfoHeaderLine) {
    Map<String, NestedField> postProcessedFields = new HashMap<>();

    Field parent = FieldImpl.builder().id(vcfInfoHeaderLine.getID())
        .label(vcfInfoHeaderLine.getID()).description(vcfInfoHeaderLine.getDescription())
        .fieldType(FieldType.INFO).type(ValueType.STRING).numberType(NumberType.OTHER)
        .separator('|').build();

    Map<String, Integer> nestedFieldIndices = getNestedInfoIds(vcfInfoHeaderLine);
    for (Entry<String, NestedField> entry : nestedMetadata.getNestedFields().entrySet()) {
      NestedField field = entry.getValue();
      Integer index = nestedFieldIndices.get(entry.getKey());
      if (index != null) {
        postProcessedFields.put(entry.getKey(),
            NestedField.builder().id(entry.getKey()).fieldType(FieldType.INFO_VEP)
                .numberType(field.getNumberType()).separator(field.getSeparator())
                .categories(field.getCategories()).index(index).numberCount(field.getNumberCount())
                .type(field.getType()).label(field.getLabel()).description(field.getDescription())
                .required(field.isRequired()).parent(parent).build());
      }
    }

    return NestedMetadata.builder().parent(parent)
        .nestedFields(addUnmappedFields(postProcessedFields, parent, nestedFieldIndices)).build();
  }

  private Map<String, NestedField> addUnmappedFields(Map<String, NestedField> metadataFields,
      Field parent, Map<String, Integer> nestedFieldIndices) {
    nestedFieldIndices.entrySet().stream()
        .filter(entry -> !metadataFields.containsKey(entry.getKey())).forEach(
            entry -> metadataFields.put(entry.getKey(),
                NestedField.builder().id(entry.getKey()).label(entry.getKey()).description(entry.getKey()).fieldType(FieldType.INFO_VEP)
                    .numberType(NumberType.NUMBER).index(entry.getValue()).numberCount(1)
                    .type(ValueType.STRING).required(false).parent(parent).build()));
    return metadataFields;
  }


  protected Map<String, Integer> getNestedInfoIds(VCFInfoHeaderLine vcfInfoHeaderLine) {
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
