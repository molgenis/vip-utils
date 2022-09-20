package org.molgenis.vcf.utils.vep;

import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.io.InputStream;
import org.molgenis.vcf.utils.model.NestedMetadata;

public interface MetadataService {

  NestedMetadata load(VCFInfoHeaderLine vcfInfoHeaderLine);

  NestedMetadata load(InputStream in, VCFInfoHeaderLine vcfInfoHeaderLine);
}
