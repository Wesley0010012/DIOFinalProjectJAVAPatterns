package com.finalproject.dio.finaldioproject.presentation.helpers;

import org.springframework.http.ResponseEntity;

import com.finalproject.dio.finaldioproject.presentation.errors.InternalServerError;

public class HttpHelpers {
    public static ResponseEntity<String> badRequest(Error error) {
        return ResponseEntity.badRequest().body(error.getMessage());
    }

    public static ResponseEntity<String> internalServerError() {
        return ResponseEntity.internalServerError().body(new InternalServerError().getMessage());
    }
}
