package com.ysell.config.converters.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * created by Tobenna
 * 02 January, 2021
 */
public class EnumDeserializer<T extends Enum> extends JsonDeserializer<T> {
    
    private Class<T> enumClass;
    
    public EnumDeserializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }


    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        TextNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();
        return new StringToEnumConverter<>(enumClass).convert(value);
    }
}
