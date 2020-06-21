package com.example.ysell.model;

public class Response<M, T> {

    private M meta;
    private T body;

    public M getMeta(){
        return meta;
    }
    public T getBody() {
        return body;
    }

}