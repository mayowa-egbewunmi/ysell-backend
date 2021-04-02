package com.ysell.config;

import com.ysell.common.converters.date.StringToDateConverter;
import com.ysell.common.converters.date.StringToLocalDateConverter;
import com.ysell.common.converters.date.StringToLocalDateTimeConverter;
import com.ysell.common.converters.enums.StringToEnumConverterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * created by Tobenna
 * 24th January, 2020
 */
@Configuration
public class EnumConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        addEnumFormatters(registry);
        addDateFormatters(registry);
    }


    private void addEnumFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }


    private void addDateFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());
        registry.addConverter(new StringToLocalDateConverter());
        registry.addConverter(new StringToLocalDateTimeConverter());
    }
}