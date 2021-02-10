package com.ysell.common.models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * created by Tobenna
 * 09 February, 2021
 */
@Getter
public enum YsellErrorCode {

    COMMON_ERROR("0001");


    private String code;


    YsellErrorCode(String code) {
        this.code = code;
    }


    @Override
    @JsonValue
    public String toString() {
        return code;
    }
}
