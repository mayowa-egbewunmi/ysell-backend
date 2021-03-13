package com.ysell.modules.common.utilities;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

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


    public static <TEntity> void validateSortFields(Pageable page, Class<TEntity> entityClass) {
        validateSortFields(page, entityClass, false);
    }


    public static <TEntity> void validateSortFields(Pageable page, Class<TEntity> entityClass, boolean checkInheritedFields) {
        if (page.getSort().isEmpty())
            return;

        Collection<String> validSortFields = getSimpleFields(entityClass);

        if (checkInheritedFields) {
            Collection<String> inheritedFields = getInheritedFields(entityClass);
            validSortFields.addAll(inheritedFields);
        }

        for(Sort.Order order : page.getSort()) {
            boolean fieldNotExist = validSortFields.stream()
                    .noneMatch(field -> field.equals(order.getProperty()));

            if (fieldNotExist)
                ServiceUtils.throwFieldNotExistException(order.getProperty(), validSortFields);
        }
    }


    private static <TEntity> Collection<String> getSimpleFields(Class<TEntity> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> BeanUtils.isSimpleValueType(field.getType()))
                .map(Field::getName)
                .sorted()
                .collect(Collectors.toList());
    }


    private static <TEntity> Collection<String> getInheritedFields(Class<TEntity> entityClass) {
        Collection<String> validSortFields = new HashSet<>();

        Class<? super TEntity> superClass = entityClass.getSuperclass();

        while (superClass != null && superClass != Object.class) {
            Collection<String> baseFields = getSimpleFields(entityClass);
            validSortFields.addAll(baseFields);
            superClass = superClass.getSuperclass();
        }

        return validSortFields;
    }
}
