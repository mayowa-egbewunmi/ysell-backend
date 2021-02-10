package com.ysell.common.converters.date;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * created by Tobenna
 * 23 January, 2021
 */
@Slf4j
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(@Nonnull String dateValue) {
        try{
            DateTimeFormatter format = DateTimeFormatter.ofPattern(DateConstants.dateFormat);
            return LocalDate.parse(dateValue, format);
        }
        catch (Exception e) {
            log.error(String.format("Error occurred converting %s to date: ", dateValue), e);
            throw new YSellRuntimeException(String.format(
                    "Wrong format for %s. Please enter in format %s", dateValue, DateConstants.dateFormat
            ));
        }
    }
}
