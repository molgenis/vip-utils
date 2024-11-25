package org.molgenis.vcf.utils.metadata;

import htsjdk.variant.vcf.VCFHeader;
import org.molgenis.vcf.utils.model.metadata.FieldMetadatas;

public interface FieldMetadataService {

  FieldMetadatas load(VCFHeader vcfHeader);
}
