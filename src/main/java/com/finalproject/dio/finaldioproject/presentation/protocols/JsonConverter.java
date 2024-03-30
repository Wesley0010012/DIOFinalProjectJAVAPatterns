package com.finalproject.dio.finaldioproject.presentation.protocols;

public interface JsonConverter<T> {
    public String convert(T data);
}
