package org.molgenis.vcf.utils.metadata;

import htsjdk.variant.vcf.VCFHeader;
import org.molgenis.vcf.utils.model.metadata.FieldMetadatas;

import java.util.Map;

public interface FieldMetadataService {

  FieldMetadatas load(VCFHeader vcfHeader, Map<FieldIdentifier, NestedAttributes> nestedAttributesMap);
  void testNonSenseFunction(int is);
}
