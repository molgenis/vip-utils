package org.molgenis.vcf.utils.metadata;

import java.io.InputStream;
import java.nio.file.Path;

public interface MetadataServiceFactory {
    MetadataService create(Path path);
}
