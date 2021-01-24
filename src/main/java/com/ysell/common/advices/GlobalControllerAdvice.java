package com.ysell.common.advices;

import com.ysell.common.models.YsellResponse;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public YsellResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest httpServletRequest) {
        log.error("Invalid request", e);
        if (e.getBindingResult().getFieldError() == null) {
            return YsellResponse.createError("invalid arguments");
        }

        FieldError error = e.getBindingResult().getFieldError();

        return YsellResponse.createError(String.format("Field %s %s", error.getField(), error.getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public YsellResponse handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest httpServletRequest) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage());
            strBuilder.append("\n");
        }

        return YsellResponse.createError(strBuilder.toString());
    }

    @ExceptionHandler(YSellRuntimeException.class)
    @ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
    public YsellResponse handleYSellRuntimeException(YSellRuntimeException e, HttpServletRequest httpServletRequest) {
        return YsellResponse.createError(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public YsellResponse handleGeneralException(Exception e, HttpServletRequest httpServletRequest) {
        log.error("Unknown server error", e);
        return YsellResponse.createError(e.getMessage());
    }
}
