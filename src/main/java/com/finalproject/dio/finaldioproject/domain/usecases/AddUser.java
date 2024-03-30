package com.finalproject.dio.finaldioproject.domain.usecases;

import com.finalproject.dio.finaldioproject.data.dto.CepDTO;
import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.domain.models.UserModel;

public interface AddUser {
    public UserModel add (UserDTO user, CepDTO cep);
}