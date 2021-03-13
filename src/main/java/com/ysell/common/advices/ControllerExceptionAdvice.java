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

import static com.ysell.common.models.YsellResponse.createError;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public YsellResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest httpServletRequest) {
        log.error("Invalid request arguments: ", e);
        if (e.getBindingResult().getFieldError() == null) {
            return createError("Invalid request arguments");
        }

        FieldError error = e.getBindingResult().getFieldError();

        return createError(String.format("Field %s %s", error.getField(), error.getDefaultMessage()));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public YsellResponse<Object> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest httpServletRequest) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage());
            strBuilder.append("\n");
        }

        return createError(strBuilder.toString());
    }


    @ExceptionHandler(YSellRuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public YsellResponse<Object> handleYSellRuntimeException(YSellRuntimeException e, HttpServletRequest httpServletRequest) {
        return createError(e.getErrors());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public YsellResponse<Object> handleGeneralException(Exception e, HttpServletRequest httpServletRequest) {
        log.error("Unknown server error", e);
        Throwable throwable = e.getCause() != null && e.getCause().getMessage() != null ? e.getCause() : e;
        String errorMessage = throwable.getMessage() != null ? throwable.getMessage() :  "Exception type: " + e.getClass().getName();
        return createError(errorMessage);
    }
}
