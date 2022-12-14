package org.molgenis.vcf.utils.sample.mapper;

import static java.util.Collections.emptyList;

import htsjdk.variant.vcf.VCFHeader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.molgenis.vcf.utils.sample.model.AffectedStatus;
import org.molgenis.vcf.utils.sample.model.Person;
import org.molgenis.vcf.utils.sample.model.Sample;
import org.molgenis.vcf.utils.sample.model.Sex;
import org.springframework.stereotype.Component;

/**
 * @see VCFHeader
 * @see Person
 */
@Component
public class HtsJdkToPersonsMapper {

  static final String MISSING = "MISSING_";
  public static final String MISSING_PERSON_ID = "0";

  public List<Sample> map(VCFHeader vcfHeader, int maxNrSamples) {
    List<Sample> samples;
    int total;

    if (!vcfHeader.hasGenotypingData()) {
      samples = emptyList();
    } else {
      Map<String, Integer> sampleNameToOffsetMap = vcfHeader.getSampleNameToOffset();

      total = sampleNameToOffsetMap.size();
      int nrSamples = Math.min(total, maxNrSamples);
      samples = new ArrayList<>(nrSamples);
      for (int i = 0; i < nrSamples; ++i) {
        samples.add(null);
      }

      sampleNameToOffsetMap.forEach(
          (sampleName, offset) -> {
            if (offset < maxNrSamples) {
              // Paternal and maternal ID can be left empty, but this way we keep it consistent with
              // the Persons loaded via the ped files.
              Sample sample = Sample.builder().person(createPerson(sampleName, offset))
                  .index(offset).build();
              samples.set(offset, sample);
            }
          });
    }
    return samples;
  }

  private Person createPerson(String sampleName, Integer offset) {
    return new Person(
        MISSING + offset,
        sampleName,
        MISSING_PERSON_ID,
        MISSING_PERSON_ID,
        Sex.UNKNOWN,
        AffectedStatus.MISSING);
  }
}
