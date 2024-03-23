package com.finalproject.dio.finaldioproject.presentation.errors;

public class MissingParamError extends Error {
    public MissingParamError(String param) {
        super("Missing param: " + param);
    }    
}
