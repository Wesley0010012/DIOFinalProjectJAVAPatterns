package com.finalproject.dio.finaldioproject.presentation.helpers;

import org.springframework.http.ResponseEntity;

public class HttpHelpers {
    public static ResponseEntity<String> badRequest(Error error) {
        return ResponseEntity.badRequest().body(error.getMessage());
    }
}
