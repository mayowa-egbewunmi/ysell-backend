package com.ysell.config.converters.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by Tobenna
 * 23 January, 2021
 * Class for path variable and request param conversion of enums
 */
@Slf4j
@SuppressWarnings("unchecked")
public class StringToEnumConverter<T extends Enum> implements Converter<String, T> {
    
    private Class<T> enumClass;
    
    public StringToEnumConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }


    @Override
    public T convert(@NotNull String enumValue) {
        return convert(enumClass, enumValue);
    }


    public T convert(Class<T> enumClass, String enumValue) {
        try {
            Method creatorMethod = getCreatorMethod();
            if (creatorMethod != null)
                return (T) creatorMethod.invoke(null, enumValue);

            return getEnumIgnoreCase(enumValue);
        } catch (Exception e) {
            log.error("Error converting {} to {} type: ", enumClass.getSimpleName(), enumValue, e);
            List<String> enumValues = getEnumValues();
            String values = formatValues(enumValues);
            throw new YSellRuntimeException(String.format("Invalid %s (%s). Please enter %s",
                    enumClass.getSimpleName(),
                    enumValue,
                    values));
        }
    }

    private T getEnumIgnoreCase(String enumValue) {
            Enum[] enumConstants = enumClass.getEnumConstants();

            for(Enum enumConstant : enumConstants) {
                if (enumConstant.toString().equalsIgnoreCase(enumValue)) {
                    return (T)enumConstant;
                }
            }

            throw new YSellRuntimeException("Unable to convert enums " + enumValue);
    }


    private Method getCreatorMethod() {
        for (final Method method : enumClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(JsonCreator.class) &&
                    Modifier.isStatic(method.getModifiers()) &&
                    enumClass.isAssignableFrom(method.getReturnType())) {
                return method;
            }
        }

        return null;
    }
    
    
    private List<String> getEnumValues() {
        T[] enumConstants = enumClass.getEnumConstants();
        return Arrays.stream(enumConstants).map(Enum::toString).collect(Collectors.toList());
    }
    
    
    private String formatValues(List<String> values) {
        if (values.size() <= 2)
            return String.join(" or ", values);
    
        int lastIndex = values.size() - 1;
        String formattedValues = String.join(", ", values.subList(0, lastIndex));
        formattedValues += " or " + values.get(lastIndex);
        
        return formattedValues;
    }
}
