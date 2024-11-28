package org.molgenis.vcf.utils.sample.mapper;

import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.molgenis.vcf.utils.sample.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhenopacketMapperTest {

    private PhenopacketMapper phenopacketMapper;

    @BeforeEach
    void setUpBeforeEach() {
        phenopacketMapper = new PhenopacketMapper();
    }

    @Test
    void mapPhenotypesGeneral() {
        List<Sample> samples = new ArrayList<>();
        samples.add(
                Sample.builder()
                        .person(
                                new Person(
                                        "fam1", "id1", "paternal1", "maternal1", Sex.MALE, AffectedStatus.AFFECTED))
                        .index(-1)
                        .build());
        samples.add(
                Sample.builder()
                        .person(
                                new Person(
                                        "fam1", "id2", "paternal2", "maternal2", Sex.FEMALE, AffectedStatus.UNAFFECTED))
                        .index(-1)
                        .build());

        List<Phenopacket> expected = new ArrayList<>();

        expected.add(createPhenopacket("id1", Arrays.asList("HP:123", "test:headache", "omim:234")));

        List<Phenopacket> actual =
                phenopacketMapper.mapPhenotypes("HP:123;test:headache;omim:234", samples);

        assertEquals(expected, actual);
    }

    @Test
    void mapPhenotypesPerSample() {
        List<Phenopacket> expected = new ArrayList<>();

        expected.add(createPhenopacket("sample1", Collections.singletonList("HP:123")));
        expected.add(createPhenopacket("sample2", Arrays.asList("test:headache", "omim:234")));

        List<Phenopacket> actual =
                phenopacketMapper.mapPhenotypes(
                        "sample1/HP:123,sample2/test:headache;omim:234", Collections.emptyList());
        assertEquals(expected, actual);
    }

    @Test
    void mapInvalidPhenotypes() {
        List<Sample> samples = Collections.emptyList();
        assertThrows(
                IllegalPhenotypeArgumentException.class,
                () -> {
                    phenopacketMapper.mapPhenotypes("sample1/HP:123,sample2/headache;omim:234", samples);
                });
    }

    private Phenopacket createPhenopacket(String sampleId, List<String> phenotypes) {
        @NonNull List<PhenotypicFeature> features = new ArrayList<>();
        for (String phenotype : phenotypes) {
            PhenotypicFeature phenotypicFeature =
                    new PhenotypicFeature(new OntologyClass(phenotype, phenotype));
            features.add(phenotypicFeature);
        }
        return new Phenopacket(features, new Individual(sampleId));
    }
}
