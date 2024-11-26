package org.molgenis.vcf.utils.hpo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.StringUtils;
import org.molgenis.vcf.utils.hpo.model.input.Hpo;
import org.molgenis.vcf.utils.hpo.model.input.HpoGraphNodeMeta;
import org.molgenis.vcf.utils.hpo.model.output.HpoTerm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

public class HpoMapper {
    private static final String PREFIX = "http://purl.obolibrary.org/obo/";

    void transform(BufferedReader reader, BufferedWriter writer) {
        ObjectMapper mapper = new ObjectMapper();

        Hpo hpo;
        try {
            hpo = mapper.readValue(reader, Hpo.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Set<String> hpoTermIds = new HashSet<>();

        Stack<String> stack = new Stack<>();
        stack.push(PREFIX + "HP_0000118"); // HP:0000118 Phenotypic abnormality

        while (!stack.isEmpty()) {
            String obj = stack.pop();
            hpo.getGraphs().forEach(graph -> {
                graph.getEdges().forEach(edge -> {
                    if (edge.getPred().equals("is_a") && edge.getObj().equals(obj)) {
                        String sub = edge.getSub();
                        hpoTermIds.add(sub);
                        stack.push(sub);
                    }
                });
            });
        }

        List<HpoTerm> hpoTerms = new ArrayList<>(hpoTermIds.size());
        hpoTermIds.forEach(hpoTermId -> {
            hpo.getGraphs().forEach(graph -> {
                graph.getNodes().forEach(node -> {
                    if (node.getId().equals(hpoTermId)) {
                        String id = hpoTermId.substring(PREFIX.length()).replace('_', ':');
                        String label = node.getLbl();
                        if (label == null) throw new RuntimeException("label is null");
                        HpoGraphNodeMeta meta = node.getMeta();
                        String description = meta != null && meta.getDefinition() != null ? meta.getDefinition().getVal() : null;
                        if (description != null) {
                            description = description.replace('\n', ' ').replace('\t', ' ');
                        }
                        hpoTerms.add(HpoTerm.builder().id(id).label(label).description(description).build());
                    }
                });
            });
        });

        hpoTerms.sort(Comparator.comparing(HpoTerm::getId));

        final CustomMappingStrategy<HpoTerm> customMappingStrategy = new CustomMappingStrategy<>();
        customMappingStrategy.setType(HpoTerm.class);

        StatefulBeanToCsv<HpoTerm> statefulBeanToCsv = new StatefulBeanToCsvBuilder<HpoTerm>(writer)
                .withMappingStrategy(customMappingStrategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator('\t').withThrowExceptions(true)
                .build();
        try {
            statefulBeanToCsv.write(hpoTerms);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    private static class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
        @Override
        public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
            final int numColumns = getFieldMap().values().size();
            super.generateHeader(bean);

            String[] header = new String[numColumns];

            BeanField beanField;
            for (int i = 0; i < numColumns; i++) {
                beanField = findField(i);
                String columnHeaderName = extractHeaderName(beanField);
                header[i] = columnHeaderName;
            }
            return header;
        }

        private String extractHeaderName(final BeanField beanField) {
            if (beanField == null || beanField.getField() == null || beanField.getField().getDeclaredAnnotationsByType(
                    CsvBindByName.class).length == 0) {
                return StringUtils.EMPTY;
            }

            final CsvBindByName bindByNameAnnotation = beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0];
            return bindByNameAnnotation.column();
        }
    }
}
