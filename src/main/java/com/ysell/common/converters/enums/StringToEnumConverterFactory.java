package com.ysell.common.converters.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * created by Tobenna
 * 02 January, 2021
 */
@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> enumClass) {
        return new StringToEnumConverter<>(enumClass);
    }
}