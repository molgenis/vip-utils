package org.molgenis.vcf.utils.hpo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HpoMapperTest {
    private HpoMapper hpoMapper;

    @BeforeEach
    void setUpBeforeEach() {
        hpoMapper = new HpoMapper();
    }

    @Test
    void transform() throws IOException {
        StringWriter stringWriter = new StringWriter();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(ResourceUtils.getFile("classpath:hpo.json")), StandardCharsets.UTF_8)); BufferedWriter bufferedWriter = new BufferedWriter(stringWriter)) {
            hpoMapper.transform(bufferedReader, bufferedWriter);
        }
        String expectedHpoTsv = """
                id\tlabel\tdescription
                HP:0000002\tAbnormality of body height\tDescription with   and   characters
                HP:0000003\tMulticystic kidney dysplasia\t
                HP:0000008\tAbnormal morphology of female internal genitalia\t
                HP:0000107\tRenal cyst\t
                """;
        assertEquals(expectedHpoTsv, stringWriter.toString());
    }
}
