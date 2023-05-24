package org.molgenis.vcf.report.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.molgenis.vcf.utils.model.metadata.HtsFormat.VCF;

import htsjdk.variant.vcf.VCFContigHeaderLine;
import htsjdk.variant.vcf.VCFHeader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vcf.utils.model.metadata.HtsFile;
import org.molgenis.vcf.utils.sample.mapper.HtsFileMapper;

@ExtendWith(MockitoExtension.class)
class HtsFileMapperTest {

  @ParameterizedTest
  @CsvSource({
      "GRCh38.5, GRCh38",
      "hg19, GRCh37",
      "human_g1k_v37_phiX.fasta, GRCh37",
      "B36, NCBI36",})
  void map(String input, String expectedBuild) {
    HtsFileMapper htsFileMapper = new HtsFileMapper();
    VCFHeader header = mock(VCFHeader.class);
    VCFContigHeaderLine contig = mock(VCFContigHeaderLine.class);
    Map<String, String> contigMap = new HashMap<>();
    contigMap.put("assembly", input);
    when(contig.getGenericFields()).thenReturn(contigMap);
    when(header.getContigLines()).thenReturn(Collections.singletonList(contig));

    HtsFile expected = new HtsFile("test.vcf", VCF, expectedBuild);
    assertEquals(expected, htsFileMapper.map(header, "test.vcf"));
  }
}
