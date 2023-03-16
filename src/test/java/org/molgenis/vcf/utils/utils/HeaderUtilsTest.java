package org.molgenis.vcf.utils.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import htsjdk.variant.vcf.VCFFilterHeaderLine;
import htsjdk.variant.vcf.VCFFormatHeaderLine;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLineCount;
import htsjdk.variant.vcf.VCFHeaderLineType;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HeaderUtilsTest {

  @Mock
  VCFHeader vcfHeader;

  @Test
  void fixVcfFormatHeaderLines() {
    when(vcfHeader.getFormatHeaderLines()).thenReturn(Set.of(new VCFFormatHeaderLine("test",1, VCFHeaderLineType.String,"\"desc\"")));
    Set<VCFFormatHeaderLine> expected = Set.of(new VCFFormatHeaderLine("test",1, VCFHeaderLineType.String,"\\\"desc\""));
    assertEquals(expected, HeaderUtils.fixVcfFormatHeaderLines(vcfHeader));
  }

  @Test
  void fixVcfFormatHeaderLines2() {
    when(vcfHeader.getFormatHeaderLines()).thenReturn(Set.of(new VCFFormatHeaderLine("test",VCFHeaderLineCount.A, VCFHeaderLineType.String,"\"desc\"")));
    Set<VCFFormatHeaderLine> expected = Set.of(new VCFFormatHeaderLine("test", VCFHeaderLineCount.A, VCFHeaderLineType.String,"\\\"desc\""));
    assertEquals(expected, HeaderUtils.fixVcfFormatHeaderLines(vcfHeader));
  }

  @Test
  void fixVcfInfoHeaderLines() {
    when(vcfHeader.getInfoHeaderLines()).thenReturn(Set.of(new VCFInfoHeaderLine("test",1, VCFHeaderLineType.String,"\"desc\"")));
    Set<VCFInfoHeaderLine> expected = Set.of(new VCFInfoHeaderLine("test",1, VCFHeaderLineType.String,"\\\"desc\""));
    assertEquals(expected, HeaderUtils.fixVcfInfoHeaderLines(vcfHeader));
  }

  @Test
  void fixVcfInfoHeaderLines2() {
    when(vcfHeader.getInfoHeaderLines()).thenReturn(Set.of(new VCFInfoHeaderLine("test",VCFHeaderLineCount.A, VCFHeaderLineType.String,"\"desc\"")));
    Set<VCFInfoHeaderLine> expected = Set.of(new VCFInfoHeaderLine("test", VCFHeaderLineCount.A, VCFHeaderLineType.String,"\\\"desc\""));
    assertEquals(expected, HeaderUtils.fixVcfInfoHeaderLines(vcfHeader));
  }

  @Test
  void fixVcfFilterHeaderLines() {
    when(vcfHeader.getFilterLines()).thenReturn(List.of(new VCFFilterHeaderLine("test","\"desc\"")));
    Set<VCFFilterHeaderLine> expected = Set.of(new VCFFilterHeaderLine("test","\\\"desc\""));
    assertEquals(expected, HeaderUtils.fixVcfFilterHeaderLines(vcfHeader));
  }
}