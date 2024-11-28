package org.molgenis.vcf.utils.hpo.model.output;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HpoTerm {
    @CsvBindByName(column = "id")
    @CsvBindByPosition(position = 0)
    String id;
    @CsvBindByName(column = "label")
    @CsvBindByPosition(position = 1)
    String label;
    @CsvBindByName(column = "description")
    @CsvBindByPosition(position = 2)
    String description;
}
