package org.molgenis.vip.config.panelmapper.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneLine {

  @CsvBindByName(
      column = "Approved symbol")
  String approved;

  @CsvBindByName(
      column = "Alias symbol")
  String alias;

  @CsvBindByName(
      column = "Previous symbol")
  String previous;

  @CsvBindByName(
      column = "NCBI gene ID")
  String ncbi;

}
