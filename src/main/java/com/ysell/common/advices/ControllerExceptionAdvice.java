package com.ysell.common.advices;

import com.ysell.common.models.YsellResponse;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
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
    public YsellResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest) {
        log.error("Invalid Request Arguments Exception: ", ex);

        if (ex.getBindingResult().getFieldError() == null) {
            return createError("Invalid request arguments");
        }

        FieldError error = ex.getBindingResult().getFieldError();

        return createError(String.format("Field %s %s", error.getField(), error.getDefaultMessage()));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public YsellResponse<Object> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest httpServletRequest) {
        log.error("Constraint Violation Exception: ", ex);

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage());
            strBuilder.append("\n");
        }

        return createError(strBuilder.toString());
    }


    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public YsellResponse<Object> handlePropertyReferenceException(PropertyReferenceException ex, HttpServletRequest httpServletRequest) {
        log.error("Property Reference Exception: ", ex);

        Throwable throwable = ex.getCause() != null && ex.getCause().getMessage() != null ? ex.getCause() : ex;

        return createError(throwable.getMessage());
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public YsellResponse<Object> handleDataIntegrityViolationErrors(DataIntegrityViolationException ex) {
        log.error("Data Integrity Exception: ", ex);

        Throwable throwable = ex.getRootCause() != null && ex.getRootCause().getMessage() != null ? ex.getRootCause() : ex;

        return createError(throwable.getMessage());
    }


    @ExceptionHandler(YSellRuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public YsellResponse<Object> handleYSellRuntimeException(YSellRuntimeException ex, HttpServletRequest httpServletRequest) {
        log.error("Generic Exception: ", ex);

        return createError(ex.getErrors());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public YsellResponse<Object> handleGeneralException(Exception ex, HttpServletRequest httpServletRequest) {
        log.error("Internal Server Exception: ", ex);

        Throwable throwable = ex.getCause() != null && ex.getCause().getMessage() != null ? ex.getCause() : ex;
        String errorMessage = throwable.getMessage() != null ? throwable.getMessage() :  "Exception type: " + ex.getClass().getName();

        return createError(errorMessage);
    }
}
