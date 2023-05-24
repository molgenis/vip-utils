package org.molgenis.vcf.utils.sample.mapper;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import org.molgenis.vcf.utils.PedIndividual;
import org.molgenis.vcf.utils.PedIndividual.AffectionStatus;
import org.molgenis.vcf.utils.PedReader;
import org.molgenis.vcf.utils.sample.model.AffectedStatus;
import org.molgenis.vcf.utils.sample.model.Pedigree;
import org.molgenis.vcf.utils.sample.model.Person;
import org.molgenis.vcf.utils.sample.model.Sample;
import org.molgenis.vcf.utils.sample.model.Sex;

public class PedToSamplesMapper {

  private PedToSamplesMapper(){}

  public static Map<String, Pedigree> mapPedFileToPedigrees(List<Path> pedigreePaths) {
    Map<String, Map<String, Sample>> pedigrees = new HashMap<>();
    Map<String, Sample> samples = mapPedFileToPersons(pedigreePaths);
    for(Sample sample : samples.values()){
      Map<String,Sample> familySamples;
      Person person = sample.getPerson();
      String familyId = person.getFamilyId();
      if(pedigrees.containsKey(familyId)){
        familySamples = pedigrees.get(familyId);
      }else{
        familySamples = new HashMap<>();
      }
      familySamples.put(sample.getPerson().getIndividualId(), sample);
      pedigrees.put(familyId, familySamples);
    }
    Map<String, Pedigree> result = new HashMap<>();
    for(Entry<String, Map<String, Sample>> entry : pedigrees.entrySet()){
      result.put(entry.getKey(), Pedigree.builder().id(entry.getKey()).members(entry.getValue()).build());
    }
    return result;
  }

  public static Map<String, Sample> mapPedFileToPersons(List<Path> pedigreePaths) {
    return mapPedFileToPersons(pedigreePaths, -1);
  }

  public static Map<String, Sample> mapPedFileToPersons(List<Path> pedigreePaths, int maxNrSamples) {
    Map<String, Sample> persons = new HashMap<>();
    for (Path pedigreePath : pedigreePaths) {
      try (PedReader reader = new PedReader(new FileReader(pedigreePath.toFile()))) {
        if(maxNrSamples != -1) {
          maxNrSamples = maxNrSamples - persons.size();
          if (maxNrSamples > 0) {
            persons.putAll(parse(reader, maxNrSamples));
          }
        }else{
          persons.putAll(parse(reader));
        }
      } catch (IOException e) {
        // this should never happen since the files were validated in the AppCommandLineOptions
        throw new IllegalStateException(e);
      }
    }
    return persons;
  }

  static Map<String, Sample> parse(PedReader reader, int maxNrSamples) {
    final Map<String, Sample> pedigreePersons = new HashMap<>();
    StreamSupport.stream(Spliterators.spliteratorUnknownSize(reader.iterator(), 0), false)
        .limit(maxNrSamples)
        .map(PedToSamplesMapper::map)
        .forEach(person -> pedigreePersons
            .put(person.getIndividualId(), Sample.builder().person(person).index(-1).build()));
    return pedigreePersons;
  }

  static Map<String, Sample> parse(PedReader reader) {
    final Map<String, Sample> pedigreePersons = new HashMap<>();
    StreamSupport.stream(Spliterators.spliteratorUnknownSize(reader.iterator(), 0), false)
        .map(PedToSamplesMapper::map)
        .forEach(person -> pedigreePersons
            .put(person.getIndividualId(), Sample.builder().person(person).index(-1).build()));
    return pedigreePersons;
  }

  static Person map(PedIndividual pedIndividual) {
    return Person.builder().familyId(pedIndividual.getFamilyId())
        .individualId(pedIndividual.getId())
        .paternalId(pedIndividual.getPaternalId()).maternalId(pedIndividual.getMaternalId())
        .sex(map(pedIndividual.getSex())).affectedStatus(map(pedIndividual.getAffectionStatus()))
        .build();
  }

  private static Sex map(PedIndividual.Sex sex) {
    return switch (sex) {
      case MALE -> Sex.MALE;
      case FEMALE -> Sex.FEMALE;
      default -> Sex.UNKNOWN;
    };
  }

  private static AffectedStatus map(AffectionStatus affectionStatus) {
    return switch (affectionStatus) {
      case AFFECTED -> AffectedStatus.AFFECTED;
      case UNAFFECTED -> AffectedStatus.UNAFFECTED;
      default -> AffectedStatus.MISSING;
    };
  }
}
