package com.finalproject.dio.finaldioproject.presentation.controllers;

import org.springframework.http.ResponseEntity;

import com.finalproject.dio.finaldioproject.data.dto.CepDTO;
import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.domain.models.UserModel;
import com.finalproject.dio.finaldioproject.domain.usecases.AddUser;
import com.finalproject.dio.finaldioproject.domain.usecases.FindUser;
import com.finalproject.dio.finaldioproject.presentation.errors.CepNotFoundedError;
import com.finalproject.dio.finaldioproject.presentation.errors.InvalidParamError;
import com.finalproject.dio.finaldioproject.presentation.errors.MissingParamError;
import com.finalproject.dio.finaldioproject.presentation.errors.UserExistsError;
import com.finalproject.dio.finaldioproject.presentation.helpers.HttpHelpers;
import com.finalproject.dio.finaldioproject.presentation.protocols.CepValidator;
import com.finalproject.dio.finaldioproject.presentation.protocols.Controller;
import com.finalproject.dio.finaldioproject.presentation.protocols.EmailValidator;
import com.finalproject.dio.finaldioproject.presentation.protocols.FindCepProxy;
import com.finalproject.dio.finaldioproject.presentation.protocols.HttpRequest;
import com.finalproject.dio.finaldioproject.presentation.protocols.NameValidator;

public class AddUserController implements Controller<UserDTO> {

    private NameValidator nameValidator = null;
    private EmailValidator emailValidator = null;
    private CepValidator cepValidator = null;
    private FindUser findUser = null;
    private FindCepProxy findCepProxy = null;
    private AddUser addUser = null;

    public AddUserController(NameValidator nameValidator, EmailValidator emailValidator, CepValidator cepValidator, FindUser findUser, FindCepProxy findCepProxy, AddUser addUser) {
        this.nameValidator = nameValidator;
        this.emailValidator = emailValidator;
        this.cepValidator = cepValidator;
        this.findUser = findUser;
        this.findCepProxy = findCepProxy;
        this.addUser = addUser;
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

            if (!this.cepValidator.isValid(cep)) {
                return HttpHelpers.badRequest(new InvalidParamError("cep"));
            }

            UserDTO tempUser = new UserDTO(name, email, cep);
            UserModel search = this.findUser.find(tempUser);

            if(search != null && search.getCep() != null && (search.getName() == tempUser.getName() ||
            search.getEmail() == tempUser.getEmail() ||
            search.getCep().getCode() == tempUser.getCep())) {
                return HttpHelpers.badRequest(new UserExistsError(tempUser));
            }

            CepDTO cepDTO = this.findCepProxy.find(tempUser.getCep());

            if (cepDTO == null) {
                return HttpHelpers.badRequest(new CepNotFoundedError(cep));
            }

            UserModel result = this.addUser.add(tempUser, cepDTO);

            return ResponseEntity.ok().body("Passed all");
        } catch(Error e) {
            return HttpHelpers.internalServerError();
        } catch(Exception e) {
            return HttpHelpers.internalServerError();
        }
    }
    
}
