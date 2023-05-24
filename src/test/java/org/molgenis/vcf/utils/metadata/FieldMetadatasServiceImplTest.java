package org.molgenis.vcf.utils.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vcf.utils.model.NestedField;
import org.molgenis.vcf.utils.model.FieldMetadata;
import org.molgenis.vcf.utils.model.NumberType;
import org.molgenis.vcf.utils.model.ValueType;

@ExtendWith(MockitoExtension.class)
class FieldMetadatasServiceImplTest {

  @Test
  void load() throws FileNotFoundException {
    VCFInfoHeaderLine vcfInfoHeaderLine = mock(VCFInfoHeaderLine.class);
    when(vcfInfoHeaderLine.getID()).thenReturn("CSQ");

    Path path = Paths.get("src", "test", "resources", "test_metadata.json");
    FieldMetadataServiceImpl metadataService = new FieldMetadataServiceImpl();

    FieldMetadata actual = metadataService.load(new FileInputStream(path.toString()),
        vcfInfoHeaderLine);

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
    FieldMetadata expected = FieldMetadata.builder().nestedFields(vepMeta).build();
    assertEquals(expected, actual);
  }


}