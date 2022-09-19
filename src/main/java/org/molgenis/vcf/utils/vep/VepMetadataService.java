package org.molgenis.vcf.utils.vep;

import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.io.InputStream;

public interface VepMetadataService {

  NestedMetadata load(VCFInfoHeaderLine vcfInfoHeaderLine);

  NestedMetadata load(InputStream in, VCFInfoHeaderLine vcfInfoHeaderLine);
}
