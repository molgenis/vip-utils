package org.molgenis.vcf.utils.vep;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import htsjdk.variant.vcf.VCFHeaderLineCount;
import htsjdk.variant.vcf.VCFHeaderLineType;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vcf.utils.metadata.FieldMetadataService;
import org.molgenis.vcf.utils.model.Field;
import org.molgenis.vcf.utils.model.FieldMetadata;
import org.molgenis.vcf.utils.model.NestedField;
import org.molgenis.vcf.utils.model.NumberType;
import org.molgenis.vcf.utils.model.ValueType;

@ExtendWith(MockitoExtension.class)
class VepMetadataServiceTest {

  @Mock
  FieldMetadataService fieldMetadataService;
  @Mock
  VCFInfoHeaderLine vcfInfoHeaderLine;

  @BeforeEach
  private void setUp(){
    when(vcfInfoHeaderLine.getID()).thenReturn("CSQ");
    when(vcfInfoHeaderLine.getDescription()).thenReturn(
        "Consequence annotations from Ensembl VEP. Format: VKGL_CL|VIPP|CAPICE_CL|TEST");

    NestedField nestedField2 = NestedField.builder().index(-1)
        .numberType(NumberType.NUMBER)
        .numberCount(1).type(ValueType.CATEGORICAL).categories(
            Set.of("P", "LP", "VUS", "LB", "B")).required(false).label("CAPICE_CL").description("CAPICE_CL")
        .build();
    NestedField nestedField3 = NestedField.builder().index(-1).separator('&')
        .numberType(NumberType.OTHER)
        .type(ValueType.INTEGER).required(false).label("PHENO").description("PHENO").build();
    NestedField nestedField4 = NestedField.builder().index(-1).separator('&')
        .numberType(NumberType.OTHER)
        .type(ValueType.STRING).required(false).label("VIP path")
        .description("VIP decision tree path").build();
    Map<String, NestedField> vepMeta = Map.of("CAPICE_CL", nestedField2, "PHENO", nestedField3, "VIPP",
        nestedField4);

    when(fieldMetadataService.load(vcfInfoHeaderLine)).thenReturn(
        FieldMetadata.builder().nestedFields(vepMeta).build());
  }

  @Test
  void load() {
VepMetadataService vepMetadataService = new VepMetadataService(fieldMetadataService);
    NestedField nestedField1 = NestedField.builder().index(0)
        .numberType(NumberType.NUMBER)
        .numberCount(1).type(ValueType.STRING).required(false).label("VKGL_CL").description("VKGL_CL")
        .build();
    NestedField nestedField2 = NestedField.builder().index(2)
        .numberType(NumberType.NUMBER)
        .numberCount(1).type(ValueType.CATEGORICAL).categories(
            Set.of("P", "LP", "VUS", "LB", "B")).required(false).label("CAPICE_CL").description("CAPICE_CL")
        .build();
    NestedField nestedField3 = NestedField.builder().index(3)
        .numberType(NumberType.NUMBER).numberCount(1)
        .type(ValueType.STRING).required(false).label("TEST").description("TEST").build();
    NestedField nestedField4 = NestedField.builder().index(1)
        .numberType(NumberType.OTHER)
        .type(ValueType.STRING).required(false).label("VIP path").separator('&')
        .description("VIP decision tree path").build();
    Map<String, NestedField> vepMeta = Map.of(
        "VKGL_CL", nestedField1, "CAPICE_CL", nestedField2, "TEST", nestedField3, "VIPP",
        nestedField4);
    Field parent = Field.builder().numberType(NumberType.OTHER)
        .type(ValueType.STRING).label("CSQ").description(
            "Consequence annotations from Ensembl VEP. Format: VKGL_CL|VIPP|CAPICE_CL|TEST")
        .required(false).separator('|').build();
    FieldMetadata expected = FieldMetadata.builder().field(parent).nestedFields(vepMeta).build();
    assertEquals(expected, vepMetadataService.load(vcfInfoHeaderLine));
  }
}