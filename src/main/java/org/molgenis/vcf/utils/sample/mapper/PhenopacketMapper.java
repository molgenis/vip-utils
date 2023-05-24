package org.molgenis.vcf.utils.sample.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import org.molgenis.vcf.utils.MixedPhenotypesException;
import org.molgenis.vcf.utils.UnexpectedEnumException;
import org.molgenis.vcf.utils.sample.model.AffectedStatus;
import org.molgenis.vcf.utils.sample.model.Individual;
import org.molgenis.vcf.utils.sample.model.OntologyClass;
import org.molgenis.vcf.utils.sample.model.Person;
import org.molgenis.vcf.utils.sample.model.Phenopacket;
import org.molgenis.vcf.utils.sample.model.PhenotypicFeature;
import org.molgenis.vcf.utils.sample.model.Sample;
import org.molgenis.vcf.utils.InvalidSamplePhenotypesException;
import org.springframework.stereotype.Component;

@Component
public class PhenopacketMapper {
  public static final String SAMPLE_PHENOTYPE_SEPARATOR = "/";
  public static final String PHENOTYPE_SEPARATOR = ";";

  public List<Phenopacket> mapPhenotypes(String phenotypes, List<Sample> samples) {
    List<Phenopacket> phenopackets = new ArrayList<>();
    List<SamplePhenotype> phenotypeList = parse(phenotypes);
    for (SamplePhenotype samplePhenotype : phenotypeList) {
      PhenotypeMode mode = samplePhenotype.getMode();
      switch (mode) {
        case STRING -> createPhenopacketsForSamples(samples, phenopackets, samplePhenotype);
        case PER_SAMPLE_STRING -> mapPhenotypes(
                phenopackets, samplePhenotype.getSubjectId(), samplePhenotype.getPhenotypes());
        default -> throw new UnexpectedEnumException(mode);
      }
    }
    return phenopackets;
  }

  private void createPhenopacketsForSamples(
      List<Sample> samples, List<Phenopacket> phenopackets, SamplePhenotype samplePhenotype) {
    for (Sample sample : samples) {
      Person person = sample.getPerson();
      if (person.getAffectedStatus() != AffectedStatus.UNAFFECTED) {
        mapPhenotypes(phenopackets, person.getIndividualId(), samplePhenotype.getPhenotypes());
      }
    }
  }

  private void mapPhenotypes(
      List<Phenopacket> phenopackets, String sampleId, String[] phenotypeString) {

    Individual individual = new Individual(sampleId);

    @NonNull List<PhenotypicFeature> features = new ArrayList<>();
    for (String phenotype : phenotypeString) {
      checkPhenotype(phenotype);
      OntologyClass ontologyClass = new OntologyClass(phenotype, phenotype);
      PhenotypicFeature phenotypicFeature = new PhenotypicFeature(ontologyClass);
      features.add(phenotypicFeature);
    }
    phenopackets.add(new Phenopacket(features, individual));
  }

  public static void checkPhenotype(String phenotype) {
    Pattern p = Pattern.compile(".+:.+");
    Matcher m = p.matcher(phenotype);
    if (!m.matches()) {
      throw new IllegalPhenotypeArgumentException(phenotype);
    }
  }

  private List<SamplePhenotype> parse(String phenotypesString) {
    if (phenotypesString.contains(SAMPLE_PHENOTYPE_SEPARATOR)) {
      return parseSamplePhenotypes(phenotypesString);
    } else {
      String[] phenotypes = phenotypesString.split(PHENOTYPE_SEPARATOR);
      return Collections.singletonList(new SamplePhenotype(PhenotypeMode.STRING, null, phenotypes));
    }
  }

  private List<SamplePhenotype> parseSamplePhenotypes(String phenotypesString) {
    List<SamplePhenotype> result = new ArrayList<>();
    for (String samplePhenotypes : phenotypesString.split(",")) {
      if (samplePhenotypes.contains("/")) {
        String[] split = samplePhenotypes.split("/");
        if (split.length == 2) {
          String sampleId = split[0];
          String[] phenotypes = split[1].split(";");
          result.add(new SamplePhenotype(PhenotypeMode.PER_SAMPLE_STRING, sampleId, phenotypes));
        } else {
          throw new InvalidSamplePhenotypesException(samplePhenotypes);
        }
      } else {
        throw new MixedPhenotypesException();
      }
    }
    return result;
  }
}
