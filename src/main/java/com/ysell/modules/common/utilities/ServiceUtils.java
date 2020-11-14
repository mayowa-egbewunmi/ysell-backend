package com.ysell.modules.common.utilities;

import com.ysell.modules.common.exceptions.YSellRuntimeException;

public class ServiceUtils {

    public static <TId> YSellRuntimeException wrongIdException(String modelName, TId id) {
        return new YSellRuntimeException(String.format("%s with Id '%s' does not exist", modelName, id));
    }

    public static <TId> YSellRuntimeException inactiveException(String modelName, TId id) {
        return new YSellRuntimeException(String.format("%s with Id '%s' is not active", modelName, id));
    }
}
