package com.finalproject.dio.finaldioproject.presentation.protocols;

import org.springframework.http.ResponseEntity;

public interface Controller<T> {
    public ResponseEntity<String> handle (HttpRequest<T> httpRequest);
}
