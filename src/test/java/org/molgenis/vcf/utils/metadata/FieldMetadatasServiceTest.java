package org.molgenis.vcf.utils.metadata;

import htsjdk.variant.vcf.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vcf.utils.model.metadata.FieldMetadata;
import org.molgenis.vcf.utils.model.metadata.FieldMetadatas;
import org.molgenis.vcf.utils.model.metadata.NestedFieldMetadata;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldMetadataServiceTest {
    private FieldMetadataService fieldMetadataService;

    @Test
    void test(){
        assertFalse(FieldMetadataServiceImpl.testNonSenseFunction(0).equals("1!"));
    }

    @Test
    void load() {
        VCFHeader vcfHeader = mock(VCFHeader.class);
        VCFInfoHeaderLine csqInfoHeaderLine = mock(VCFInfoHeaderLine.class);
        when(csqInfoHeaderLine.getID()).thenReturn("CSQ");
        when(csqInfoHeaderLine.getDescription()).thenReturn("TEST:VIPP|TEST|TEST2");
        when(csqInfoHeaderLine.getType()).thenReturn(VCFHeaderLineType.String);
        when(csqInfoHeaderLine.getCountType()).thenReturn(VCFHeaderLineCount.UNBOUNDED);

        VCFInfoHeaderLine testinfoHeaderLine = mock(VCFInfoHeaderLine.class);
        when(testinfoHeaderLine.getID()).thenReturn("TEST");

        VCFInfoHeaderLine test3infoHeaderLine = mock(VCFInfoHeaderLine.class);
        when(test3infoHeaderLine.getID()).thenReturn("TEST3");
        when(test3infoHeaderLine.getType()).thenReturn(VCFHeaderLineType.Float);
        when(test3infoHeaderLine.getCountType()).thenReturn(VCFHeaderLineCount.R);

        VCFFormatHeaderLine formatHeaderLine1 = mock(VCFFormatHeaderLine.class);
        when(formatHeaderLine1.getID()).thenReturn("FORMAT1");

        VCFFormatHeaderLine formatNestedHeaderLine = mock(VCFFormatHeaderLine.class);
        when(formatNestedHeaderLine.getID()).thenReturn("NESTED_FORMAT");
        when(formatNestedHeaderLine.getDescription()).thenReturn("NESTED_FORMAT:TEST");
        when(formatNestedHeaderLine.getType()).thenReturn(VCFHeaderLineType.String);
        when(formatNestedHeaderLine.getCountType()).thenReturn(VCFHeaderLineCount.R);

        VCFFormatHeaderLine formatHeaderLine2 = mock(VCFFormatHeaderLine.class);
        when(formatHeaderLine2.getID()).thenReturn("FORMAT2");
        when(formatHeaderLine2.getType()).thenReturn(VCFHeaderLineType.Float);
        when(formatHeaderLine2.getCountType()).thenReturn(VCFHeaderLineCount.A);

        when(vcfHeader.getInfoHeaderLines()).thenReturn(Set.of(csqInfoHeaderLine, testinfoHeaderLine, test3infoHeaderLine));
        when(vcfHeader.getFormatHeaderLines()).thenReturn(Set.of(formatHeaderLine1, formatHeaderLine2, formatNestedHeaderLine));

        NestedFieldMetadata nestedVippMeta = NestedFieldMetadata.builder().index(0).label("VIP path").description("VIP decision tree path").separator('&').type(ValueType.STRING).numberType(ValueCount.Type.VARIABLE).build();
        NestedFieldMetadata nestedTestMeta = NestedFieldMetadata.builder().index(1).label("TEST label").description("TEST desc").type(ValueType.INTEGER).numberType(ValueCount.Type.R).build();
        NestedFieldMetadata nestedTest2Meta = NestedFieldMetadata.builder().index(2).label("TEST2").type(ValueType.STRING).numberType(ValueCount.Type.FIXED).numberCount(1).build();
        FieldMetadata csqMeta = FieldMetadata.builder().label("VEP").description("TEST:VIPP|TEST|TEST2").numberType(ValueCount.Type.VARIABLE).type(ValueType.STRING).numberType(ValueCount.Type.VARIABLE).nestedFields(Map.of("VIPP", nestedVippMeta, "TEST", nestedTestMeta, "TEST2", nestedTest2Meta)).build();
        FieldMetadata testMeta = FieldMetadata.builder().label("TEST INFO").description("TEST INFO desc").type(ValueType.FLAG).numberType(ValueCount.Type.FIXED).numberCount(1).build();
        FieldMetadata test3Meta = FieldMetadata.builder().label("TEST3").type(ValueType.FLOAT).numberType(ValueCount.Type.R).build();
        FieldMetadata format1Meta = FieldMetadata.builder().label("TEST").description("TEST").type(ValueType.INTEGER).numberType(ValueCount.Type.FIXED).numberCount(1).build();
        FieldMetadata format2Meta = FieldMetadata.builder().label("FORMAT2").type(ValueType.FLOAT).numberType(ValueCount.Type.A).build();
        NestedFieldMetadata formatNestedTest = NestedFieldMetadata.builder().label("NESTED label").description("NESTED desc").type(ValueType.INTEGER).numberType(ValueCount.Type.R).index(0).build();
        FieldMetadata formatNestedMeta = FieldMetadata.builder().label("NESTED_FORMAT").description("NESTED_FORMAT:TEST").type(ValueType.STRING).numberType(ValueCount.Type.R).nestedFields(Map.of("TEST", formatNestedTest)).build();

        FieldMetadatas expected = FieldMetadatas.builder().info(Map.of("CSQ", csqMeta, "TEST", testMeta, "TEST3", test3Meta)).format(Map.of("FORMAT1", format1Meta, "FORMAT2", format2Meta, "NESTED_FORMAT", formatNestedMeta)).build();
        Path path = Paths.get("src", "test", "resources", "test_metadata.json");
        fieldMetadataService = new FieldMetadataServiceImpl(path.toFile());
        FieldMetadatas actual = fieldMetadataService.load(vcfHeader, Map.of(FieldIdentifier.builder().name("CSQ").type(FieldType.INFO).build(),
                NestedAttributes.builder().prefix("TEST:").seperator("|").build(),
                FieldIdentifier.builder().name("NESTED_FORMAT").type(FieldType.FORMAT).build(),
                NestedAttributes.builder().prefix("NESTED_FORMAT:").seperator("|").build()));
        assertEquals(expected, actual);
    }
}