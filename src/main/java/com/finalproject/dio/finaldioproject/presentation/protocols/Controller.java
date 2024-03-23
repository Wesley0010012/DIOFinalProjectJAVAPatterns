package com.finalproject.dio.finaldioproject.presentation.protocols;

import org.springframework.http.ResponseEntity;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;

public interface Controller {
    public ResponseEntity<String> handle (UserDTO httpRequest);
}
