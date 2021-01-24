package com.ysell.config.converters.date;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * created by Tobenna
 * 23 January, 2021
 */
@Slf4j
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(@Nonnull String dateValue) {
        try{
            DateFormat format = new SimpleDateFormat(DateConstants.dateFormat);
            return format.parse(dateValue);
        }
        catch (Exception e) {
            log.error(String.format("Error occurred converting %s to date: ", dateValue), e);
            throw new YSellRuntimeException(String.format(
                    "Wrong format for %s. Please enter in format %s", dateValue, DateConstants.dateFormat
            ));
        }
    }
}
