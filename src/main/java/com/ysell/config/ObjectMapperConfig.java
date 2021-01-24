package com.ysell.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ysell.config.converters.date.*;
import com.ysell.config.converters.enums.EnumDeserializer;
import com.ysell.config.converters.enums.StringToEnumConverterFactory;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 * created by Tobenna
 * 24 January, 2020
 */
@Configuration
public class ObjectMapperConfig  implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;


    @PostConstruct
    public void addExtraModules() {
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverterFactory());

        registry.addConverter(new StringToDateConverter());
        registry.addConverter(new StringToLocalDateConverter());
        registry.addConverter(new StringToLocalDateTimeConverter());
    }


    @PostConstruct
    @SuppressWarnings("unchecked")
    public void addEnumDeserializers() {
        SimpleModule module = new SimpleModule();

        getAllProjectEnumClasses().forEach(enumClass -> {
            EnumDeserializer enumDeserializer = new EnumDeserializer<>(enumClass);
            module.addDeserializer(enumClass, enumDeserializer);
        });

        module.addDeserializer(Date.class, new DateDeserializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        objectMapper.registerModule(module);
    }


    private Collection<Class<? extends Enum>> getAllProjectEnumClasses() {
        Reflections reflections = new Reflections("com.ysell");
        return reflections.getSubTypesOf(Enum.class);
    }
}
