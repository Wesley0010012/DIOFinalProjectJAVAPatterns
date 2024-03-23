package com.finalproject.dio.finaldioproject.presentation.errors;

public class CepNotFoundedError extends Error {
    public CepNotFoundedError(String cep) {
        super("Cep not founded: " + cep);
    }
}
