package com.ysell.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ysell.common.converters.date.*;
import com.ysell.common.converters.enums.EnumDeserializer;
import com.ysell.common.converters.enums.StringToEnumConverterFactory;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * created by Tobenna
 * 24 January, 2020
 */
@Configuration
public class ObjectMapperConfig  implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        addEnumFormatters(registry);
        addDateFormatters(registry);
    }


    @PostConstruct
    public void addDeserializers() {
        SimpleModule module = new SimpleModule();
        addEnumDeserializers(module);

        objectMapper.registerModule(new JavaTimeModule());
        addDateDeserializers(module);

        objectMapper.registerModule(module);
    }


    private void addEnumFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }


    private void addDateFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());
        registry.addConverter(new StringToLocalDateConverter());
        registry.addConverter(new StringToLocalDateTimeConverter());
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
