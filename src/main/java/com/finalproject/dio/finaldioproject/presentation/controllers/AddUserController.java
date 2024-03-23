package com.finalproject.dio.finaldioproject.presentation.controllers;

import org.springframework.http.ResponseEntity;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.presentation.errors.MissingParamError;
import com.finalproject.dio.finaldioproject.presentation.helpers.HttpHelpers;
import com.finalproject.dio.finaldioproject.presentation.protocols.Controller;
import com.finalproject.dio.finaldioproject.presentation.protocols.HttpRequest;

public class AddUserController implements Controller<UserDTO> {

    @Override
    public ResponseEntity<String> handle(HttpRequest<UserDTO> httpRequest) {
        if (httpRequest.getBody().getName() == null || httpRequest.getBody().getName() == "") {
            return HttpHelpers.badRequest(new MissingParamError("name"));
        }

        if (httpRequest.getBody().getEmail() == null || httpRequest.getBody().getEmail() == "") {
            return HttpHelpers.badRequest(new MissingParamError("email"));
        }

        if (httpRequest.getBody().getCep() == null || httpRequest.getBody().getCep() == "") {
            return HttpHelpers.badRequest(new MissingParamError("cep"));
        }

        return ResponseEntity.ok().body("Passed all");
    }
    
}
