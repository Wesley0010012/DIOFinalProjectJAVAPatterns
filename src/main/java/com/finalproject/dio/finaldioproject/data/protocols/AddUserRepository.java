package com.finalproject.dio.finaldioproject.data.protocols;

import com.finalproject.dio.finaldioproject.domain.models.UserModel;

public interface AddUserRepository {
    public UserModel add (UserModel user);
}
