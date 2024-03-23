package com.finalproject.dio.finaldioproject.domain.usecases;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.domain.models.UserModel;

public interface FindUser {
    public UserModel find (UserDTO user);
}