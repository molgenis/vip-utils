package org.molgenis.vcf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.molgenis.vcf.utils.sample.model.Sample;
import org.springframework.stereotype.Component;

@Component
public class PersonListMerger {
  public List<Sample> merge(
      List<Sample> vcfSamples, Map<String, Sample> pedigreeSamples, int maxNrSamples) {
    vcfSamples.forEach(
        sample -> {
          if (pedigreeSamples.containsKey(sample.getPerson().getIndividualId())
              && pedigreeSamples.size() < maxNrSamples) {
            Sample merged =
                Sample.builder()
                    .person(pedigreeSamples.get(sample.getPerson().getIndividualId()).getPerson())
                    .index(sample.getIndex())
                    .build();
            pedigreeSamples.put(sample.getPerson().getIndividualId(), merged);
          } else {
            pedigreeSamples.put(sample.getPerson().getIndividualId(), sample);
          }
        });
    return new ArrayList<>(pedigreeSamples.values());
  }
}
