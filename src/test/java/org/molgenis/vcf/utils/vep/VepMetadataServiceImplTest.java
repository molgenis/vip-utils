package org.molgenis.vcf.utils.vep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.molgenis.vcf.utils.model.FieldType.INFO;
import static org.molgenis.vcf.utils.model.FieldType.INFO_VEP;

import htsjdk.variant.vcf.VCFHeaderLineCount;
import htsjdk.variant.vcf.VCFHeaderLineType;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.molgenis.vcf.utils.model.Field;
import org.molgenis.vcf.utils.model.FieldImpl;
import org.molgenis.vcf.utils.model.FieldImpl.NumberType;
import org.molgenis.vcf.utils.model.NestedField;
import org.molgenis.vcf.utils.model.ValueType;

class VepMetadataServiceImplTest {

  @Test
  void load() throws FileNotFoundException {
    VCFInfoHeaderLine vcfInfoHeaderLine = mock(VCFInfoHeaderLine.class);
    when(vcfInfoHeaderLine.getID()).thenReturn("Test");
    when(vcfInfoHeaderLine.getType()).thenReturn(VCFHeaderLineType.String);
    when(vcfInfoHeaderLine.getCountType()).thenReturn(VCFHeaderLineCount.UNBOUNDED);
    when(vcfInfoHeaderLine.getDescription()).thenReturn(
        "Consequence annotations from Ensembl VEP. Format: VKGL_CL|PHENO|VIPP|CAPICE_CL");

    Path path = Paths.get("src", "test", "resources", "testMetadata.json");
    VepMetadataService vepMetadataService = new VepMetadataServiceImpl();

    NestedMetadata actual = vepMetadataService.load(new FileInputStream(path.toString()),
        vcfInfoHeaderLine);

    Field parent = FieldImpl.builder().fieldType(INFO).numberType(NumberType.OTHER)
        .type(ValueType.STRING).id("Test").required(false).separator('|').build();
    NestedField nestedField1 = NestedField.builder().id("VKGL_CL").index(0)
        .numberType(NumberType.NUMBER)
        .numberCount(1).type(ValueType.STRING).fieldType(INFO_VEP).parent(parent).required(false)
        .build();
    NestedField nestedField2 = NestedField.builder().id("CAPICE_CL").index(3)
        .numberType(NumberType.NUMBER)
        .numberCount(1).type(ValueType.CATEGORICAL).categories(
            Set.of("P", "LP", "VUS", "LB", "B")).fieldType(INFO_VEP).parent(parent).required(false)
        .build();
    NestedField nestedField3 = NestedField.builder().id("PHENO").index(1).separator('&')
        .numberType(NumberType.OTHER)
        .type(ValueType.INTEGER).fieldType(INFO_VEP).parent(parent).required(false).build();
    NestedField nestedField4 = NestedField.builder().id("VIPP").index(2).separator('&')
        .numberType(NumberType.OTHER)
        .type(ValueType.STRING).fieldType(INFO_VEP).parent(parent).required(false).label("VIP path")
        .description("VIP decision tree path").build();
    Map<String, NestedField> vepMeta = Map.of(
        "VKGL_CL", nestedField1, "CAPICE_CL", nestedField2, "PHENO", nestedField3, "VIPP",
        nestedField4);
    NestedMetadata expected = NestedMetadata.builder().parent(parent).vepMetadata(vepMeta).build();
    assertEquals(expected, actual);
  }


}