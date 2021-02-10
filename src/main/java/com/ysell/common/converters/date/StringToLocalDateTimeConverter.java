package com.ysell.common.converters.date;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * created by Tobenna
 * 23 January, 2021
 */
@Slf4j
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(@Nonnull String dateValue) {
        try{
            DateTimeFormatter format = DateTimeFormatter.ofPattern(DateConstants.dateTimeFormat);
            return LocalDateTime.parse(dateValue, format);
        }
        catch (Exception e) {
            log.error(String.format("Error occurred converting %s to local date time: ", dateValue), e);
            throw new YSellRuntimeException(String.format(
                    "Wrong format for %s. Please enter in format %s", dateValue, DateConstants.dateTimeFormat
            ));
        }
    }
}
