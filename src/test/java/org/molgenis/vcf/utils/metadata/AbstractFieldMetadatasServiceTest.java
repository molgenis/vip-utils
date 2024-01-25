package org.molgenis.vcf.utils.metadata;

import htsjdk.variant.vcf.VCFInfoHeaderLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vcf.utils.model.FieldMetadata;
import org.molgenis.vcf.utils.model.NestedField;
import org.molgenis.vcf.utils.model.NumberType;
import org.molgenis.vcf.utils.model.ValueType;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractFieldMetadataServiceTest {
  private AbstractFieldMetadataService fieldMetadataService;
  @BeforeEach
  void setUp() {
    fieldMetadataService = new AbstractFieldMetadataService(){
      @Override
      public FieldMetadata load(VCFInfoHeaderLine vcfInfoHeaderLine) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test
  void load() throws FileNotFoundException {
    VCFInfoHeaderLine vcfInfoHeaderLine = mock(VCFInfoHeaderLine.class);
    when(vcfInfoHeaderLine.getID()).thenReturn("CSQ");

    Path path = Paths.get("src", "test", "resources", "test_metadata.json");

    FieldMetadata actual = fieldMetadataService.load(new FileInputStream(path.toString()),
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
  @Test
  void loadIllegalInput() {
    VCFInfoHeaderLine vcfInfoHeaderLine = mock(VCFInfoHeaderLine.class);
    String jsonInvalid="invalid_json";
    assertThrows(UncheckedIOException.class, () -> fieldMetadataService.load(new ByteArrayInputStream(jsonInvalid.getBytes(StandardCharsets.UTF_8)), vcfInfoHeaderLine));
  }
}