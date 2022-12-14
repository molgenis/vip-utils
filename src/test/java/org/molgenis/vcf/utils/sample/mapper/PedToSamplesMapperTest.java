package org.molgenis.vcf.utils.sample.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.molgenis.vcf.utils.sample.model.Sex.FEMALE;
import static org.molgenis.vcf.utils.sample.model.Sex.MALE;
import static org.molgenis.vcf.utils.sample.model.Sex.UNKNOWN;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vcf.utils.PedIndividual;
import org.molgenis.vcf.utils.PedIndividual.AffectionStatus;
import org.molgenis.vcf.utils.PedIndividual.Sex;
import org.molgenis.vcf.utils.PedReader;
import org.molgenis.vcf.utils.sample.model.AffectedStatus;
import org.molgenis.vcf.utils.sample.model.Person;
import org.molgenis.vcf.utils.sample.model.Sample;
import org.springframework.util.ResourceUtils;

@ExtendWith(MockitoExtension.class)
class PedToSamplesMapperTest {

  @Test
  void mapPedFileToPersons() throws FileNotFoundException {
    Path pedFile1 = ResourceUtils.getFile("classpath:example.ped").toPath();
    Path pedFile2 = ResourceUtils.getFile("classpath:example2.ped").toPath();
    List<Path> paths = Arrays.asList(pedFile1, pedFile2);

    Map<String, Sample> expected = new HashMap<>();
    expected.put(
        "John",
        Sample.builder()
            .person(new Person("FAM001", "John", "Jimmy", "Jane", MALE, AffectedStatus.AFFECTED))
            .index(-1)
            .build());
    expected.put(
        "Jimmy",
        Sample.builder()
            .person(new Person("FAM001", "Jimmy", "0", "0", MALE, AffectedStatus.UNAFFECTED))
            .index(-1)
            .build());
    expected.put(
        "Jane",
        Sample.builder()
            .person(new Person("FAM001", "Jane", "0", "0", FEMALE, AffectedStatus.UNAFFECTED))
            .index(-1)
            .build());
    expected.put(
        "James",
        Sample.builder()
            .person(new Person("FAM002", "James", "0", "0", MALE, AffectedStatus.UNAFFECTED))
            .index(-1)
            .build());
    expected.put(
        "Jake",
        Sample.builder()
            .person(new Person("FAM003", "Jake", "0", "0", MALE, AffectedStatus.AFFECTED))
            .index(-1)
            .build());

    assertEquals(expected, PedToSamplesMapper.mapPedFileToPersons(paths, 10));
  }

  @Test
  void mapPedFileToPersonsMaxSamples() throws FileNotFoundException {
    Path pedFile1 = ResourceUtils.getFile("classpath:example.ped").toPath();
    Path pedFile2 = ResourceUtils.getFile("classpath:example2.ped").toPath();
    List<Path> paths = Arrays.asList(pedFile1, pedFile2);

    Map<String, Sample> expected = new HashMap<>();
    expected.put(
        "John",
        Sample.builder()
            .person(new Person("FAM001", "John", "Jimmy", "Jane", MALE, AffectedStatus.AFFECTED))
            .index(-1)
            .build());
    expected.put(
        "Jimmy",
        Sample.builder()
            .person(new Person("FAM001", "Jimmy", "0", "0", MALE, AffectedStatus.UNAFFECTED))
            .index(-1)
            .build());

    assertEquals(expected, PedToSamplesMapper.mapPedFileToPersons(paths, 2));
  }

  @Mock private PedReader pedReader;

  @Test
  void parse() {
    PedIndividual individual1 =
        new PedIndividual(
            "fam1", "id1", "paternal1", "maternal1", Sex.MALE, AffectionStatus.AFFECTED);
    PedIndividual individual2 =
        new PedIndividual(
            "fam1", "id2", "paternal2", "maternal2", Sex.FEMALE, AffectionStatus.UNAFFECTED);

    List<PedIndividual> pedIndividuals = Arrays.asList(individual1, individual2);
    when(pedReader.iterator()).thenReturn(pedIndividuals.iterator());

    Map<String, Sample> expected = new HashMap<>();
    expected.put(
        "id1",
        Sample.builder()
            .person(
                new Person("fam1", "id1", "paternal1", "maternal1", MALE, AffectedStatus.AFFECTED))
            .index(-1)
            .build());
    expected.put(
        "id2",
        Sample.builder()
            .person(
                new Person(
                    "fam1", "id2", "paternal2", "maternal2", FEMALE, AffectedStatus.UNAFFECTED))
            .index(-1)
            .build());

    assertEquals(expected, PedToSamplesMapper.parse(pedReader, 10));
  }

  @Test
  void parseUnknown() {
    PedIndividual individual =
        new PedIndividual(
            "fam1", "id1", "paternal", "maternal", Sex.UNKNOWN, AffectionStatus.UNKNOWN);
    List<PedIndividual> pedIndividuals = List.of(individual);
    when(pedReader.iterator()).thenReturn(pedIndividuals.iterator());

    Map<String, Sample> expected = new HashMap<>();
    expected.put(
        "id1",
        Sample.builder()
            .person(
                new Person(
                    "fam1", "id1", "paternal", "maternal", UNKNOWN, AffectedStatus.MISSING))
            .index(-1)
            .build());

    assertEquals(expected, PedToSamplesMapper.parse(pedReader, 10));
  }
}
