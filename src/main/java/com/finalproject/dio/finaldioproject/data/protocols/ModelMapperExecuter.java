package com.finalproject.dio.finaldioproject.data.protocols;

public interface ModelMapperExecuter<T, O> {
    public T convert(O o);
}
