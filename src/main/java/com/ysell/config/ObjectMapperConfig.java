package com.ysell.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ysell.common.converters.date.DateDeserializer;
import com.ysell.common.converters.date.LocalDateDeserializer;
import com.ysell.common.converters.date.LocalDateTimeDeserializer;
import com.ysell.common.converters.enums.EnumDeserializer;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * created by Tobenna
 * 24 January, 2020
 */
@Configuration
public class ObjectMapperConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        return addDeserializers(objectMapper);
    }


    public ObjectMapper addDeserializers(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        addEnumDeserializers(module);

        objectMapper.registerModule(new JavaTimeModule());
        addDateDeserializers(module);

        objectMapper.registerModule(module);

        return objectMapper;
    }


    @SuppressWarnings("unchecked")
    private void addEnumDeserializers(SimpleModule module) {
        Reflections reflections = new Reflections("com.ysell");
        reflections.getSubTypesOf(Enum.class).forEach(enumClass -> {
            EnumDeserializer enumDeserializer = new EnumDeserializer<>(enumClass);
            module.addDeserializer(enumClass, enumDeserializer);
        });
    }


    private void addDateDeserializers(SimpleModule module) {
        module.addDeserializer(Date.class, new DateDeserializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    }
}