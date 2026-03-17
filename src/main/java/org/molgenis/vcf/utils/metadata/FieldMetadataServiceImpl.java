package org.molgenis.vcf.utils.metadata;

import static java.util.Objects.requireNonNull;
import static org.molgenis.vcf.utils.metadata.FieldType.FORMAT;
import static org.molgenis.vcf.utils.metadata.FieldType.INFO;
import static org.molgenis.vcf.utils.metadata.ValueCount.Type.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import htsjdk.variant.vcf.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.molgenis.vcf.utils.UnexpectedEnumException;
import org.molgenis.vcf.utils.model.NumberType;
import org.molgenis.vcf.utils.model.ValueDescription;
import org.molgenis.vcf.utils.model.json.JsonFieldMetadata;
import org.molgenis.vcf.utils.model.json.JsonFieldMetadatas;
import org.molgenis.vcf.utils.model.json.NestedJsonFieldMetadata;
import org.molgenis.vcf.utils.model.metadata.FieldMetadata;
import org.molgenis.vcf.utils.model.metadata.FieldMetadatas;
import org.molgenis.vcf.utils.model.metadata.NestedFieldMetadata;

public class FieldMetadataServiceImpl implements FieldMetadataService {
  private final File jsonMetadata;

  public FieldMetadataServiceImpl(File jsonMetadata) {
    this.jsonMetadata = requireNonNull(jsonMetadata);
  }

  @Override
  public FieldMetadatas load(VCFHeader vcfHeader) {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, FieldMetadata> infoMetadata = new HashMap<>();
    Map<String, FieldMetadata> formatMetadata = new HashMap<>();

    try (InputStream inputStream = new FileInputStream(jsonMetadata)) {
      JsonFieldMetadatas jsonFieldMetadatas =
          mapper.readValue(inputStream, JsonFieldMetadatas.class);
      vcfHeader
          .getInfoHeaderLines()
          .forEach(line -> infoMetadata.put(line.getID(), mapInfoHeader(line, jsonFieldMetadatas)));
      vcfHeader
          .getFormatHeaderLines()
          .forEach(
              line -> formatMetadata.put(line.getID(), mapFormatHeader(line, jsonFieldMetadatas)));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return FieldMetadatas.builder().info(infoMetadata).format(formatMetadata).build();
  }

  private FieldMetadata mapInfoHeader(
      VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas) {
    if (jsonFieldMetadatas.getInfo().containsKey(line.getID())) {
      return mapCustomInfoFieldMetadata(line, jsonFieldMetadatas);
    } else {
      ValueType type = mapVcfValueType(line.getType());
      ValueCount.Type numberType = mapVcfNumberType(line.getCountType());
      return FieldMetadata.builder()
          .label(line.getID())
          .description(line.getDescription())
          .type(type)
          .numberType(numberType)
          .numberCount(getCount(line))
          .required(false)
          .build();
    }
  }

  private FieldMetadata mapFormatHeader(
      VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas) {
    if (jsonFieldMetadatas.getFormat().containsKey(line.getID())) {
      return mapCustomFormatFieldMetadata(line, jsonFieldMetadatas);
    } else {
      ValueType type = mapVcfValueType(line.getType());
      ValueCount.Type numberType = mapVcfNumberType(line.getCountType());
      return FieldMetadata.builder()
          .label(line.getID())
          .description(line.getDescription())
          .type(type)
          .numberType(numberType)
          .numberCount(getCount(line))
          .required(false)
          .build();
    }
  }

  private @Nullable Integer getCount(VCFCompoundHeaderLine line) {
    return line.getCountType() == VCFHeaderLineCount.INTEGER ? line.getCount() : null;
  }

  private FieldMetadata mapCustomInfoFieldMetadata(
      VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas) {
    JsonFieldMetadata jsonFieldMetadata = jsonFieldMetadatas.getInfo().get(line.getID());
    if (jsonFieldMetadata == null) {
      throw new NoSuchElementException(line.getID());
    }
    return mapCustomFieldMetadata(line, jsonFieldMetadatas, jsonFieldMetadata);
  }

  private FieldMetadata mapCustomFieldMetadata(
      VCFCompoundHeaderLine line,
      JsonFieldMetadatas jsonFieldMetadatas,
      JsonFieldMetadata jsonFieldMetadata) {
    ValueType type =
        jsonFieldMetadata.getType() != null
            ? jsonFieldMetadata.getType()
            : mapVcfValueType(line.getType());
    ValueCount.Type numberType =
        jsonFieldMetadata.getNumberType() != null
            ? mapNumberType(jsonFieldMetadata.getNumberType())
            : mapVcfNumberType(line.getCountType());
    Integer numberCount =
        jsonFieldMetadata.getNumberCount() != null
            ? jsonFieldMetadata.getNumberCount()
            : getCount(line);
    boolean required = jsonFieldMetadata.getRequired() != null && jsonFieldMetadata.getRequired();
    Character separator =
        jsonFieldMetadata.getSeparator() != null ? jsonFieldMetadata.getSeparator() : null;
    NestedAttributes nestedAttributes =
        jsonFieldMetadata.getNestedAttributes() != null
            ? jsonFieldMetadata.getNestedAttributes()
            : null;
    Map<String, ValueDescription> categories =
        jsonFieldMetadata.getCategories() != null ? jsonFieldMetadata.getCategories() : null;
    String label =
        jsonFieldMetadata.getLabel() != null ? jsonFieldMetadata.getLabel() : line.getID();
    String description =
        jsonFieldMetadata.getDescription() != null
            ? jsonFieldMetadata.getDescription()
            : line.getDescription();
    Map<String, NestedFieldMetadata> nestedFields =
        jsonFieldMetadata.getNestedFields() != null
            ? mapNestedFields(line, jsonFieldMetadatas)
            : null;
    ValueDescription nullValue =
        jsonFieldMetadata.getNullValue() != null ? jsonFieldMetadata.getNullValue() : null;
    return FieldMetadata.builder()
        .label(line.getID())
        .description(description)
        .type(type)
        .numberType(numberType)
        .numberCount(numberCount)
        .categories(categories)
        .separator(separator)
        .label(label)
        .nestedFields(nestedFields)
        .nestedAttributes(nestedAttributes)
        .nullValue(nullValue)
        .required(required)
        .build();
  }

  private FieldMetadata mapCustomFormatFieldMetadata(
      VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas) {
    JsonFieldMetadata jsonFieldMetadata = jsonFieldMetadatas.getFormat().get(line.getID());
    if (jsonFieldMetadata == null) {
      throw new NoSuchElementException(line.getID());
    }
    return mapCustomFieldMetadata(line, jsonFieldMetadatas, jsonFieldMetadata);
  }

  private @Nullable Map<String, NestedFieldMetadata> mapNestedFields(
      VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas) {
    Map<String, NestedFieldMetadata> nestedFields = new HashMap<>();
    FieldType type = line instanceof VCFInfoHeaderLine ? INFO : FORMAT;
    FieldIdentifier identifier = FieldIdentifier.builder().name(line.getID()).type(type).build();
    JsonFieldMetadata parent =
        type == INFO
            ? jsonFieldMetadatas.getInfo().get(identifier.getName())
            : jsonFieldMetadatas.getFormat().get(identifier.getName());
    if (parent != null) {
      validateParent(parent, identifier);
      mapConfigNestedField(line, jsonFieldMetadatas, parent, nestedFields);
    } else {
      nestedFields =
          requireNonNull(
                  getNestedFields(
                      jsonFieldMetadatas,
                      line.getID(),
                      line instanceof VCFInfoHeaderLine ? INFO : FORMAT))
              .entrySet()
              .stream()
              .collect(
                  Collectors.toMap(
                      Map.Entry::getKey, entry -> entry.getValue().toFieldNestedFieldMetadata()));
    }
    return nestedFields;
  }

  private static void validateParent(JsonFieldMetadata parent, FieldIdentifier identifier) {
    if (parent.getNestedAttributes() == null) {
      throw new MissingNestedAttributesException(identifier.getName());
    } else if (parent.getNestedAttributes().getSeparator() == null) {
      throw new MissingSeparatorException(identifier.getName());
    }
  }

  private static void mapConfigNestedField(
      VCFCompoundHeaderLine line,
      JsonFieldMetadatas jsonFieldMetadatas,
      JsonFieldMetadata parent,
      Map<String, NestedFieldMetadata> nestedFields) {
    NestedAttributes nestedAttributes = requireNonNull(parent.getNestedAttributes());
    String escapedSeparator = Pattern.quote(nestedAttributes.getSeparator());
    if (nestedAttributes.getPrefix() != null) {
      String description = line.getDescription();
      String[] infoIds =
          description.substring(nestedAttributes.getPrefix().length()).split(escapedSeparator, -1);
      int index = 0;
      for (String id : infoIds) {
        NestedFieldMetadata.NestedFieldMetadataBuilder<?, ?> nestedFieldMetadataBuilder =
            NestedFieldMetadata.builder().label(id).index(index);
        NestedJsonFieldMetadata jsonMeta = getNestedMetadata(line, jsonFieldMetadatas, id);
        if (jsonMeta != null) {
          nestedFieldMetadataBuilder
              .label(jsonMeta.getLabel())
              .description(jsonMeta.getDescription())
              .type(jsonMeta.getType())
              .numberType(mapNumberType(jsonMeta.getNumberType()))
              .numberCount(jsonMeta.getNumberCount())
              .categories(jsonMeta.getCategories())
              .separator(jsonMeta.getSeparator())
              .nullValue(jsonMeta.getNullValue())
              .required(jsonMeta.getRequired() != null && jsonMeta.getRequired())
              .build();
        } else {
          // No metadata available, assume non-required single string value
          nestedFieldMetadataBuilder
              .type(ValueType.STRING)
              .numberType(FIXED)
              .numberCount(1)
              .required(false)
              .build();
        }
        nestedFields.put(id, nestedFieldMetadataBuilder.build());
        index++;
      }
    } else {
      for (Map.Entry<String, NestedJsonFieldMetadata> entry :
          requireNonNull(parent.getNestedFields()).entrySet()) {
        NestedJsonFieldMetadata jsonMeta = entry.getValue();
        if (jsonMeta.getIndex() == -1) {
          throw new NestedMetadataWithoutIndexException(entry.getKey());
        }
        NestedFieldMetadata.NestedFieldMetadataBuilder<?, ?> nestedFieldMetadataBuilder =
            NestedFieldMetadata.builder()
                .index(jsonMeta.getIndex())
                .label(jsonMeta.getLabel())
                .description(jsonMeta.getDescription())
                .type(jsonMeta.getType())
                .numberType(mapNumberType(jsonMeta.getNumberType()))
                .numberCount(jsonMeta.getNumberCount())
                .categories(jsonMeta.getCategories())
                .separator(jsonMeta.getSeparator())
                .nullValue(jsonMeta.getNullValue())
                .required(jsonMeta.getRequired() != null && jsonMeta.getRequired());
        nestedFields.put(entry.getKey(), nestedFieldMetadataBuilder.build());
      }
    }
  }

  private static @Nullable NestedJsonFieldMetadata getNestedMetadata(
      VCFCompoundHeaderLine line, JsonFieldMetadatas jsonFieldMetadatas, String id) {
    JsonFieldMetadata jsonFieldMetadata;
    if (line instanceof VCFInfoHeaderLine) {
      jsonFieldMetadata = jsonFieldMetadatas.getInfo().get(line.getID());
    } else {
      jsonFieldMetadata = jsonFieldMetadatas.getFormat().get(line.getID());
    }
    if (jsonFieldMetadata == null) {
      throw new NoSuchElementException(line.getID());
    }

    Map<String, NestedJsonFieldMetadata> nestedJsonMeta = jsonFieldMetadata.getNestedFields();
    NestedJsonFieldMetadata jsonMeta = null;
    if (nestedJsonMeta != null) {
      jsonMeta = nestedJsonMeta.getOrDefault(id, null);
    }
    return jsonMeta;
  }

  private static @Nullable Map<String, NestedJsonFieldMetadata> getNestedFields(
      JsonFieldMetadatas jsonFieldMetadatas, String id, FieldType type) {
    JsonFieldMetadata jsonFieldMetadata;
    if (type == INFO) {
      jsonFieldMetadata = jsonFieldMetadatas.getInfo().get(id);
    } else {
      jsonFieldMetadata = jsonFieldMetadatas.getFormat().get(id);
    }
    if (jsonFieldMetadata == null) {
      throw new NoSuchElementException(id);
    }

    return jsonFieldMetadata.getNestedFields();
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

  public static ValueCount.Type mapNumberType(NumberType numberType) {
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
