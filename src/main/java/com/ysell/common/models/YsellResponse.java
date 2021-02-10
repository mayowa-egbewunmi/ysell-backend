package com.ysell.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class YsellResponse<T> {

    private boolean successful;

    private List<Error> errorMessages;

    private final T body;


    public static <T> YsellResponse<T> createSuccess(T model) {
        return new YsellResponse<>(true, null, model);
    }


    public static <T> YsellResponse<T> createError(String... errorMessages) {
        return new YsellResponse<>(false,
                Arrays.stream(errorMessages).map(Error::from).collect(Collectors.toList()),
                null);
    }


    public static <T> YsellResponse<T> createError(List<String> errorMessages) {
        return new YsellResponse<>(false,
                errorMessages.stream().map(Error::from).collect(Collectors.toList()),
                null);
    }


    public static <T> YsellResponse<T> createError(String errorMessage, YsellErrorCode errorCode) {
        return new YsellResponse<>(false,
                Collections.singletonList(new Error(errorMessage, errorCode)),
                null);
    }


    public static <T> YsellResponse<T> createError(Error error) {
        return new YsellResponse<>(false, Collections.singletonList(error), null);
    }


    public static <T> YsellResponse<T> createError(Collection<Error> errors) {
        return new YsellResponse<>(false, new ArrayList<>(errors), null);
    }


    @AllArgsConstructor
    @Getter
    public static class Error {

        private String message;

        private YsellErrorCode code;


        public static Error from(String errorMessage) {
            return new Error(errorMessage, YsellErrorCode.COMMON_ERROR);
        }
    }
}