package com.finalproject.dio.finaldioproject.presentation.controllers;

import org.springframework.http.ResponseEntity;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.presentation.errors.InvalidParamError;
import com.finalproject.dio.finaldioproject.presentation.errors.MissingParamError;
import com.finalproject.dio.finaldioproject.presentation.helpers.HttpHelpers;
import com.finalproject.dio.finaldioproject.presentation.protocols.Controller;
import com.finalproject.dio.finaldioproject.presentation.protocols.EmailValidator;
import com.finalproject.dio.finaldioproject.presentation.protocols.HttpRequest;
import com.finalproject.dio.finaldioproject.presentation.protocols.NameValidator;

public class AddUserController implements Controller<UserDTO> {

    private NameValidator nameValidator = null;
    private EmailValidator emailValidator = null;

    public AddUserController(NameValidator nameValidator, EmailValidator emailValidator) {
        this.nameValidator = nameValidator;
        this.emailValidator = emailValidator;
    }

    @Override
    public ResponseEntity<String> handle(HttpRequest<UserDTO> httpRequest) {
        try {
            if (httpRequest.getBody().getName() == null || httpRequest.getBody().getName() == "") {
                return HttpHelpers.badRequest(new MissingParamError("name"));
            }
    
            if (httpRequest.getBody().getEmail() == null || httpRequest.getBody().getEmail() == "") {
                return HttpHelpers.badRequest(new MissingParamError("email"));
            }
    
            if (httpRequest.getBody().getCep() == null || httpRequest.getBody().getCep() == "") {
                return HttpHelpers.badRequest(new MissingParamError("cep"));
            }
    
            String name = httpRequest.getBody().getName();
            String email = httpRequest.getBody().getEmail();
            String cep = httpRequest.getBody().getCep();
    
            if (!this.nameValidator.isValid(name)) {
                return HttpHelpers.badRequest(new InvalidParamError("name"));
            }

            if (!this.emailValidator.isValid(email)) {
                return HttpHelpers.badRequest(new InvalidParamError("email"));
            }
    
            return ResponseEntity.ok().body("Passed all");
        } catch(Error e) {
            return HttpHelpers.internalServerError();
        }
    }
    
}
