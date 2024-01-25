package org.molgenis.vcf.utils.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import org.molgenis.vcf.utils.model.FieldMetadata;
import org.molgenis.vcf.utils.model.FieldMetadatas;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public abstract class AbstractFieldMetadataService implements FieldMetadataService {
  private FieldMetadatas fieldMetadatas;

  public abstract FieldMetadata load(VCFInfoHeaderLine vcfInfoHeaderLine);

  //Custom path used only for testing purposes
  protected FieldMetadata load(InputStream in, VCFInfoHeaderLine vcfInfoHeaderLine) {
    ObjectMapper mapper = new ObjectMapper();

    if(fieldMetadatas == null) {
      try {
        fieldMetadatas = mapper.readValue(in, FieldMetadatas.class);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }
    if (!fieldMetadatas.getInfo().containsKey(vcfInfoHeaderLine.getID())) {
      throw new UnknownFieldException(vcfInfoHeaderLine.getID());
    }
    return fieldMetadatas.getInfo().get(vcfInfoHeaderLine.getID());
  }
}
