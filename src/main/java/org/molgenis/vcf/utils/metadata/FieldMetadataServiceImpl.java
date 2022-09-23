package org.molgenis.vcf.utils.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.molgenis.vcf.utils.model.FieldMetadata;
import org.molgenis.vcf.utils.model.FieldMetadatas;
import org.springframework.stereotype.Component;

@Component
public class FieldMetadataServiceImpl implements FieldMetadataService {
  private FieldMetadatas fieldMetadatas;

  @Override
  public FieldMetadata load(VCFInfoHeaderLine vcfInfoHeaderLine) {
    InputStream in = FieldMetadataServiceImpl.class.getClassLoader()
        .getResourceAsStream("field_metadata.json");

    return load(in, vcfInfoHeaderLine);
  }

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
