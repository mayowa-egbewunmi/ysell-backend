package com.ysell.common.converters.date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.Date;

/**
 * created by Tobenna
 * 02 January, 2021
 */
public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        TextNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();
        return new StringToDateConverter().convert(value);
    }
}
