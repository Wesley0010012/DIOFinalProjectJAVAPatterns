package com.finalproject.dio.finaldioproject.presentation.errors;

public class InvalidParamError extends Error {
    public InvalidParamError(String param) {
        super("Invalid param: " + param);
    }
}
