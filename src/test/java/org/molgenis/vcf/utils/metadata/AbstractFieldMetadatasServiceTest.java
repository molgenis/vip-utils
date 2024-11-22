package org.molgenis.vcf.utils.metadata;

import htsjdk.variant.vcf.VCFInfoHeaderLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vcf.utils.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

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
                    Map.of("P", new ValueDescription("P", "Description P"), "LP", new ValueDescription("LP", null),
                            "VUS", new ValueDescription("VUS", null), "LB", new ValueDescription("LB", null),
                            "B", new ValueDescription("B", null))).required(false).label("CAPICE_CL").description("CAPICE_CL")
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
    InputStream inputStream = new ByteArrayInputStream("invalid_json".getBytes(StandardCharsets.UTF_8));
    assertThrows(UncheckedIOException.class, () -> fieldMetadataService.load(inputStream, vcfInfoHeaderLine));
  }
}