package org.molgenis.vcf.utils.metadata;

import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class MetadataServiceFactoryImpl implements MetadataServiceFactory{

    @Override
    public MetadataService create(Path path) {
        return new MetadataServiceImpl(path);
    }
}
