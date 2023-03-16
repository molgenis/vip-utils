package org.molgenis.vcf.utils.utils;

import htsjdk.variant.vcf.VCFFilterHeaderLine;
import htsjdk.variant.vcf.VCFFormatHeaderLine;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLineCount;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class HeaderUtils {

  private HeaderUtils(){}

  public static Collection<VCFFormatHeaderLine> fixVcfFormatHeaderLines(VCFHeader vcfHeader) {
    Collection<VCFFormatHeaderLine> formatHeaderLines = new LinkedHashSet<>(vcfHeader.getFormatHeaderLines());
    for(VCFFormatHeaderLine vcfHeaderLine : vcfHeader.getFormatHeaderLines()){
      String description = vcfHeaderLine.getDescription();
      if(description.startsWith("\"")){
        formatHeaderLines.remove(vcfHeaderLine);
        description = "\\" + description;
        if(vcfHeaderLine.getCountType() == VCFHeaderLineCount.INTEGER) {
          formatHeaderLines.add(new VCFFormatHeaderLine(vcfHeaderLine.getID(), vcfHeaderLine.getCount(),
              vcfHeaderLine.getType(),
              description));
        }else{
          formatHeaderLines.add(new VCFFormatHeaderLine(vcfHeaderLine.getID(), vcfHeaderLine.getCountType(),
              vcfHeaderLine.getType(),
              description));
        }
      }
    }
    return formatHeaderLines;
  }

  public static Set<VCFInfoHeaderLine> fixVcfInfoHeaderLines(VCFHeader vcfHeader) {
    Set<VCFInfoHeaderLine> infoHeaderLines = new LinkedHashSet<>(vcfHeader.getInfoHeaderLines());
    for(VCFInfoHeaderLine vcfHeaderLine : vcfHeader.getInfoHeaderLines()){
      String description = vcfHeaderLine.getDescription();
      if(description.startsWith("\"")){
        infoHeaderLines.remove(vcfHeaderLine);
        description = "\\" + description;
        if(vcfHeaderLine.getCountType() == VCFHeaderLineCount.INTEGER) {
          infoHeaderLines.add(new VCFInfoHeaderLine(vcfHeaderLine.getID(), vcfHeaderLine.getCount(),
              vcfHeaderLine.getType(),
              description, vcfHeaderLine.getSource(), vcfHeaderLine.getVersion()));
        }else{
          infoHeaderLines.add(new VCFInfoHeaderLine(vcfHeaderLine.getID(), vcfHeaderLine.getCountType(),
              vcfHeaderLine.getType(),
              description, vcfHeaderLine.getSource(), vcfHeaderLine.getVersion()));
        }
      }
    }
    return infoHeaderLines;
  }

  public static Collection<VCFFilterHeaderLine> fixVcfFilterHeaderLines(VCFHeader vcfHeader) {
    Collection<VCFFilterHeaderLine> filterHeaderLines = new LinkedHashSet<>(
        vcfHeader.getFilterLines());
    for (VCFFilterHeaderLine vcfHeaderLine : vcfHeader.getFilterLines()) {
      String description = vcfHeaderLine.getDescription();
      if (description.startsWith("\"")) {
        filterHeaderLines.remove(vcfHeaderLine);
        description = "\\" + description;
        filterHeaderLines.add(
            new VCFFilterHeaderLine(vcfHeaderLine.getID(),
                description));
      }
    }
    return filterHeaderLines;
  }
}
