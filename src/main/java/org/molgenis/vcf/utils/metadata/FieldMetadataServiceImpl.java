package org.molgenis.vcf.utils.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import htsjdk.variant.vcf.*;
import org.molgenis.vcf.utils.UnexpectedEnumException;
import org.molgenis.vcf.utils.model.NumberType;
import org.molgenis.vcf.utils.model.ValueDescription;
import org.molgenis.vcf.utils.model.json.JsonFieldMetadata;
import org.molgenis.vcf.utils.model.json.JsonFieldMetadatas;
import org.molgenis.vcf.utils.model.json.NestedJsonFieldMetadata;
import org.molgenis.vcf.utils.model.metadata.FieldMetadata;
import org.molgenis.vcf.utils.model.metadata.FieldMetadatas;
import org.molgenis.vcf.utils.model.metadata.NestedFieldMetadata;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;
import static org.molgenis.vcf.utils.metadata.FieldType.FORMAT;
import static org.molgenis.vcf.utils.metadata.FieldType.INFO;
import static org.molgenis.vcf.utils.metadata.ValueCount.Type.*;

public class FieldMetadataServiceImpl implements FieldMetadataService {
    private final File jsonMetadata;

    public FieldMetadataServiceImpl(File jsonMetadata) {
        this.jsonMetadata = requireNonNull(jsonMetadata);
    }

    public FieldMetadatas load(VCFHeader vcfHeader, Map<FieldIdentifier, NestedAttributes> nestedAttributesMap) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, FieldMetadata> infoMetadata = new HashMap<>();
        Map<String, FieldMetadata> formatMetadata = new HashMap<>();

        try (InputStream inputStream = new FileInputStream(jsonMetadata)) {
            JsonFieldMetadatas jsonFieldMetadatas = mapper.readValue(inputStream, JsonFieldMetadatas.class);
            vcfHeader.getInfoHeaderLines().forEach(line -> infoMetadata.put(line.getID(), mapInfoHeader(line, jsonFieldMetadatas, nestedAttributesMap)));
            vcfHeader.getFormatHeaderLines().forEach(line -> formatMetadata.put(line.getID(), mapFormatHeader(line, jsonFieldMetadatas, nestedAttributesMap)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        testNonSenseFunction(1);

        return FieldMetadatas.builder().info(infoMetadata).format(formatMetadata).build();
    }

    public void testNonSenseFunction(int i) {
        if(i>1){
            System.out.println("i!");
        }else{
            System.out.println("i?");
        }
    }

    private FieldMetadata mapInfoHeader(VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas, Map<FieldIdentifier, NestedAttributes> nestedAttributesMap) {
        if (jsonFieldMetadatas.getInfo().containsKey(line.getID())) {
            return mapCustomInfoFieldMetadata(line, jsonFieldMetadatas, nestedAttributesMap);
        } else {
            ValueType type = mapVcfValueType(line.getType());
            ValueCount.Type numberType = mapVcfNumberType(line.getCountType());
            return FieldMetadata.builder().label(line.getID()).description(line.getDescription()).type(type)
                    .numberType(numberType).numberCount(getCount(line)).required(false).build();
        }
    }

    private FieldMetadata mapFormatHeader(VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas, Map<FieldIdentifier, NestedAttributes> nestedAttributesMap) {
        if (jsonFieldMetadatas.getFormat().containsKey(line.getID())) {
            return mapCustomFormatFieldMetadata(line, jsonFieldMetadatas, nestedAttributesMap);
        } else {
            ValueType type = mapVcfValueType(line.getType());
            ValueCount.Type numberType = mapVcfNumberType(line.getCountType());
            return FieldMetadata.builder().label(line.getID()).description(line.getDescription()).type(type)
                    .numberType(numberType).numberCount(getCount(line)).required(false).build();
        }
    }

    private Integer getCount(VCFCompoundHeaderLine line) {
        return line.getCountType() == VCFHeaderLineCount.INTEGER ? line.getCount() : null;
    }

    private FieldMetadata mapCustomInfoFieldMetadata(VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas, Map<FieldIdentifier, NestedAttributes> nestedAttributesMap) {
        JsonFieldMetadata jsonFieldMetadata = jsonFieldMetadatas.getInfo().get(line.getID());
        return mapCustomFieldMetadata(line, jsonFieldMetadatas, jsonFieldMetadata, nestedAttributesMap);
    }

    private FieldMetadata mapCustomFieldMetadata(VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas, JsonFieldMetadata jsonFieldMetadata, Map<FieldIdentifier, NestedAttributes> nestedAttributesMap) {
        ValueType type = jsonFieldMetadata.getType() != null ? jsonFieldMetadata.getType() : mapVcfValueType(line.getType());
        ValueCount.Type numberType = jsonFieldMetadata.getNumberType() != null ? mapNumberType(jsonFieldMetadata.getNumberType()) : mapVcfNumberType(line.getCountType());
        Integer numberCount = jsonFieldMetadata.getNumberCount() != null ? jsonFieldMetadata.getNumberCount() : getCount(line);
        boolean required = jsonFieldMetadata.getRequired() != null ? jsonFieldMetadata.getRequired() : false;
        Character separator = jsonFieldMetadata.getSeparator() != null ? jsonFieldMetadata.getSeparator() : null;
        Map<String, ValueDescription> categories = jsonFieldMetadata.getCategories() != null ? jsonFieldMetadata.getCategories() : null;
        String label = jsonFieldMetadata.getLabel() != null ? jsonFieldMetadata.getLabel() : line.getID();
        String description = jsonFieldMetadata.getDescription() != null ? jsonFieldMetadata.getDescription() : line.getDescription();
        Map<String, NestedFieldMetadata> nestedFields = jsonFieldMetadata.getNestedFields() != null ? mapNestedFields(line, jsonFieldMetadatas, nestedAttributesMap) : null;
        ValueDescription nullValue = jsonFieldMetadata.getNullValue() != null ? jsonFieldMetadata.getNullValue() : null;
        return FieldMetadata.builder().label(line.getID()).description(description).type(type)
                .numberType(numberType).numberCount(numberCount).categories(categories).separator(separator)
                .label(label).nestedFields(nestedFields).nullValue(nullValue).required(required).build();
    }

    private FieldMetadata mapCustomFormatFieldMetadata(VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas, Map<FieldIdentifier, NestedAttributes> nestedAttributesMap) {
        JsonFieldMetadata jsonFieldMetadata = jsonFieldMetadatas.getFormat().get(line.getID());
        return mapCustomFieldMetadata(line, jsonFieldMetadatas, jsonFieldMetadata, nestedAttributesMap);
    }

    private Map<String, NestedFieldMetadata> mapNestedFields(VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas, Map<FieldIdentifier, NestedAttributes> nestedAttributesMap) {
        Map<String, NestedFieldMetadata> nestedFields = new HashMap<>();
        FieldType type = line instanceof VCFInfoHeaderLine ? INFO : FORMAT;
        FieldIdentifier identifier = FieldIdentifier.builder().name(line.getID()).type(type).build();
        if (!nestedAttributesMap.containsKey(identifier)) {
            throw new MissingPrefixException(identifier);
        }
        NestedAttributes nestedAttributes = nestedAttributesMap.get(identifier);
        String description = line.getDescription();
        String escapedSeparator = Pattern.quote(nestedAttributes.getSeperator());
        String[] infoIds = description.substring(nestedAttributes.getPrefix().length()).split(escapedSeparator, -1);
        int index = 0;
        for (String id : infoIds) {
            NestedFieldMetadata.NestedFieldMetadataBuilder<?, ?> nestedFieldMetadataBuilder = NestedFieldMetadata
                    .builder().label(id).index(index);
            NestedJsonFieldMetadata jsonMeta = getNestedMetadata(line, jsonFieldMetadatas, id);
            if (jsonMeta != null) {
                nestedFieldMetadataBuilder.label(jsonMeta.getLabel()).description(jsonMeta
                                .getDescription()).type(jsonMeta.getType())
                        .numberType(mapNumberType(jsonMeta.getNumberType())).numberCount(jsonMeta.getNumberCount())
                        .categories(jsonMeta.getCategories()).separator(jsonMeta.getSeparator())
                        .nullValue(jsonMeta.getNullValue())
                        .required(jsonMeta.getRequired() != null ? jsonMeta.getRequired() : false).build();
            } else {
                //No metadata available, assume non-required single string value
                nestedFieldMetadataBuilder.type(ValueType.STRING)
                        .numberType(FIXED).numberCount(1)
                        .required(false).build();
            }
            nestedFields.put(id, nestedFieldMetadataBuilder.build());
            index++;
        }
        return nestedFields;
    }

    private static NestedJsonFieldMetadata getNestedMetadata(VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas, String id) {
        NestedJsonFieldMetadata jsonMeta = null;
        Map<String, NestedJsonFieldMetadata> nestedJsonMeta;
        if (line instanceof VCFInfoHeaderLine) {
            nestedJsonMeta = jsonFieldMetadatas.getInfo().get(line.getID()).getNestedFields();
        } else {
            nestedJsonMeta = jsonFieldMetadatas.getFormat().get(line.getID()).getNestedFields();
        }
        if (nestedJsonMeta != null) {
            jsonMeta = nestedJsonMeta.getOrDefault(id, null);
        }
        return jsonMeta;
    }

    private ValueType mapVcfValueType(VCFHeaderLineType type) {
        return switch (type) {
            case Integer -> ValueType.INTEGER;
            case Float -> ValueType.FLOAT;
            case Flag -> ValueType.FLAG;
            case Character -> ValueType.CHARACTER;
            case String -> ValueType.STRING;
            //noinspection UnnecessaryDefault
            default -> throw new UnexpectedEnumException(type);
        };
    }

    private ValueCount.Type mapNumberType(NumberType numberType) {
        return switch (numberType) {
            case NumberType.NUMBER -> FIXED;
            case NumberType.PER_ALT -> A;
            case NumberType.PER_ALT_AND_REF -> R;
            case NumberType.PER_GENOTYPE -> G;
            case NumberType.OTHER -> VARIABLE;
            //noinspection UnnecessaryDefault
            default -> throw new UnexpectedEnumException(numberType);
        };
    }

    private ValueCount.Type mapVcfNumberType(VCFHeaderLineCount numberType) {
        return switch (numberType) {
            case VCFHeaderLineCount.INTEGER -> FIXED;
            case VCFHeaderLineCount.A -> A;
            case VCFHeaderLineCount.R -> R;
            case VCFHeaderLineCount.G -> G;
            case VCFHeaderLineCount.UNBOUNDED -> VARIABLE;
            //noinspection UnnecessaryDefault
            default -> throw new UnexpectedEnumException(numberType);
        };
    }
}
