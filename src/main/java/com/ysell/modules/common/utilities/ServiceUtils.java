package com.ysell.modules.common.utilities;

import com.ysell.modules.common.exceptions.YSellRuntimeException;

import java.util.Collection;
import java.util.UUID;

public class ServiceUtils {

    public static YSellRuntimeException wrongIdException(String modelName, UUID id) {
        return new YSellRuntimeException(String.format("%s with Id '%s' does not exist", modelName, id));
    }


    public static void throwWrongIdException(String modelName, UUID id) {
        throw wrongIdException(modelName, id);
    }


    public static YSellRuntimeException wrongNameException(String modelName, String name) {
        return new YSellRuntimeException(String.format("%s with name '%s' already exists", modelName, name));
    }


    public static void throwWrongNameException(String modelName, String name) {
        throw wrongNameException(modelName, name);
    }


    public static YSellRuntimeException wrongEmailException(String modelName, String email) {
        return new YSellRuntimeException(String.format("%s with email '%s' already exists", modelName, email));
    }


    public static void throwWrongEmailException(String modelName, String email) {
        throw wrongEmailException(modelName, email);
    }


    public static YSellRuntimeException fieldNotExistException(String field, Collection<String> validFields) {
        return new YSellRuntimeException(String.format("%s is not a valid field. Valid fields: %s",
                field, String.join(", ", validFields)));
    }


    public static void throwFieldNotExistException(String field, Collection<String> validFields) {
        throw fieldNotExistException(field, validFields);
    }
}