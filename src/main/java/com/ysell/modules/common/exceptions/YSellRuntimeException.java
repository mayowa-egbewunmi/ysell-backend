package com.ysell.modules.common.exceptions;

public class YSellRuntimeException extends RuntimeException {

    public YSellRuntimeException() {
        super();
    }

    public YSellRuntimeException(String message) {
        super(message);
    }

    public YSellRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
