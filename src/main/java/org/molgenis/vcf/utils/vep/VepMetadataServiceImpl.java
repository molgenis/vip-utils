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
import org.molgenis.vcf.utils.model.FieldImpl.NumberType;
import org.molgenis.vcf.utils.model.FieldType;
import org.molgenis.vcf.utils.model.NestedField;
import org.molgenis.vcf.utils.model.ValueType;
import org.springframework.stereotype.Component;

@Component
public class VepMetadataServiceImpl implements VepMetadataService {
  private static final String INFO_DESCRIPTION_PREFIX =
      "Consequence annotations from Ensembl VEP. Format: ";

  @Override
  public NestedMetadata load(VCFInfoHeaderLine vcfInfoHeaderLine) {
    InputStream in = VepMetadataServiceImpl.class.getClassLoader().getResourceAsStream("vepMetadata.json");

    return load(in, vcfInfoHeaderLine);
  }

  @Override
  public NestedMetadata load(InputStream in, VCFInfoHeaderLine vcfInfoHeaderLine) {
    ObjectMapper mapper = new ObjectMapper();

    NestedMetadata vepMetadata;
    try {
      vepMetadata =
          mapper.readValue(in, NestedMetadata.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return postProcess(vepMetadata, vcfInfoHeaderLine);
  }


  private NestedMetadata postProcess(NestedMetadata vepMetadata, VCFInfoHeaderLine vcfInfoHeaderLine) {
    Map<String, NestedField> postProcessedFields = new HashMap<>();

    Field parent =
        FieldImpl.builder()
            .id(vcfInfoHeaderLine.getID())
            .fieldType(FieldType.INFO)
            .type(ValueType.STRING)
            .numberType(NumberType.OTHER)
            .separator('|')
            .build();

    Map<String, Integer> nestedFieldIndices = getNestedInfoIds(vcfInfoHeaderLine);
    for (Entry<String, NestedField> entry : vepMetadata.getVepMetadata().entrySet()) {
      NestedField field = entry.getValue();
      Integer index = nestedFieldIndices.get(entry.getKey());
      if(index != null) {
        postProcessedFields.put(entry.getKey(),
            NestedField.builder().id(entry.getKey()).fieldType(FieldType.INFO_VEP)
                .numberType(field.getNumberType()).separator(field.getSeparator())
                .categories(field.getCategories()).index(index)
                .numberCount(field.getNumberCount()).type(field.getType())
                .required(field.isRequired()).parent(parent).build());
      }
    }

    return NestedMetadata.builder().parent(parent).vepMetadata(addUnmappedFields(postProcessedFields, parent, nestedFieldIndices)).build();
  }

  private Map<String, NestedField> addUnmappedFields(Map<String, NestedField> metadataFields,
      Field parent, Map<String, Integer> nestedFieldIndices) {
    nestedFieldIndices.entrySet().stream().filter(entry -> !metadataFields.containsKey(entry.getKey())).forEach(entry ->
        metadataFields.put(entry.getKey(),
            NestedField.builder().id(entry.getKey()).fieldType(FieldType.INFO_VEP)
                .numberType(NumberType.NUMBER).index(entry.getValue())
                .numberCount(1).type(ValueType.STRING)
                .required(false).parent(parent).build()));
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
