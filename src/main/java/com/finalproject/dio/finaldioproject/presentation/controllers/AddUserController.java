package com.finalproject.dio.finaldioproject.presentation.controllers;

import org.springframework.http.ResponseEntity;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.presentation.protocols.Controller;

public class AddUserController implements Controller {

    @Override
    public ResponseEntity<String> handle(UserDTO httpRequest) {
        if (httpRequest.getName() == null || httpRequest.getName() == "") {
            return ResponseEntity.badRequest().body("Missing param: name");
        }

        return ResponseEntity.ok().body("Passed all");
    }
    
}
