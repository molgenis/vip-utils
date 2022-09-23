package org.molgenis.vcf.utils.metadata;

import htsjdk.variant.vcf.VCFInfoHeaderLine;
import org.molgenis.vcf.utils.model.FieldMetadata;

public interface FieldMetadataService {

  FieldMetadata load(VCFInfoHeaderLine vcfInfoHeaderLine);
}
