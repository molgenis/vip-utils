package org.molgenis.vcf.utils.panelmapper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.SpringApplication;
import org.springframework.util.ResourceUtils;

class AppIT {

  @TempDir
  Path sharedTempDir;

  @Test
  void test() throws IOException {
    String inputFile = ResourceUtils.getFile("classpath:panel.txt").toString();
    String mappingFile = ResourceUtils.getFile("classpath:mapping.txt").toString();
    String outputFile = sharedTempDir.resolve("output_mapped.tsv").toString();

    String[] args = {"-i", inputFile, "-g", mappingFile, "-o", outputFile};
    SpringApplication.run(App.class, args);

    String output = Files.readString(Path.of(outputFile)).replaceAll("\\R", "\n");

    Path expectedPath = ResourceUtils.getFile("classpath:expected.tsv").toPath();
    String expected = Files.readString(expectedPath).replaceAll("\\R", "\n");

    assertEquals(expected, output);
  }
}