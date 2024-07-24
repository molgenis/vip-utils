package org.molgenis.vcf.utils.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.molgenis.vcf.utils.model.FieldMetadatas;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class MetadataServiceImpl implements MetadataService{
    private FieldMetadatas fieldMetadatas;

    public MetadataServiceImpl(Path metadata) {
        ObjectMapper mapper = new ObjectMapper();

            try {
                fieldMetadatas = mapper.readValue(metadata.toFile(), FieldMetadatas.class);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
    }
    @Override
    public FieldMetadatas getFieldMetadatas() {
        return fieldMetadatas;
    }
}
