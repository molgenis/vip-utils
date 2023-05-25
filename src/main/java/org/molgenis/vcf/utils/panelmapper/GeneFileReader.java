package org.molgenis.vcf.utils.panelmapper;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.molgenis.vcf.utils.panelmapper.model.GeneLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class GeneFileReader {

    private GeneFileReader(){}
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneFileReader.class);
    static List<GeneLine> readGenesFile(Path genesPath) {
        List<GeneLine> genesLines;
        try (InputStream fileStream = new FileInputStream(genesPath.toFile());
             Reader reader = new BufferedReader(new InputStreamReader(fileStream))) {
            CsvToBean<GeneLine> csvToBean =
                    new CsvToBeanBuilder<GeneLine>(reader)
                            .withSeparator('\t')
                            .withType(GeneLine.class)
                            .withThrowExceptions(false)
                            .build();
            genesLines = csvToBean.parse();
            handleCsvParseExceptions(csvToBean.getCapturedExceptions());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return genesLines;
    }

    static void handleCsvParseExceptions(List<CsvException> exceptions) {
        exceptions.forEach(csvException -> LOGGER
                .error(String.format("%s,%s", csvException.getLineNumber(), csvException.getMessage())));
    }
}
