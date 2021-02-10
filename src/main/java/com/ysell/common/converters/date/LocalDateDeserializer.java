package com.ysell.common.converters.date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.time.LocalDate;

/**
 * created by Tobenna
 * 02 January, 2021
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        TextNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();
        return new StringToLocalDateConverter().convert(value);
    }
}
