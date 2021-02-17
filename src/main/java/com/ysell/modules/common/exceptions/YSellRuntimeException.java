package com.ysell.modules.common.exceptions;

import com.ysell.common.models.YsellErrorCode;
import com.ysell.common.models.YsellResponse.Error;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class YSellRuntimeException extends RuntimeException {

    private List<Error> errors;

    public YSellRuntimeException() {
        super();
        initializeError(getMessage());
    }

    public YSellRuntimeException(String message) {
        super(message);
        initializeError(message);
    }

    public YSellRuntimeException(String message, Throwable cause) {
        super(message, cause);
        initializeError(message);
    }

    public YSellRuntimeException(String message, YsellErrorCode errorCode) {
        super(message);
        initializeError(message, errorCode);
    }

    public YSellRuntimeException(List<Error> errors) {
        super();
        this.errors = errors;
    }

    private void initializeError(String message) {
        initializeError(message, YsellErrorCode.COMMON_ERROR);
    }

    private void initializeError(String message, YsellErrorCode errorCode) {
        this.errors = Collections.singletonList(new Error(message, errorCode));
    }
}