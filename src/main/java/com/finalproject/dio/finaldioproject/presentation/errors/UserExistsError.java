package com.finalproject.dio.finaldioproject.presentation.errors;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;

public class UserExistsError extends Error {
    public UserExistsError(UserDTO user) {
        super("User Exists: " + user.toString());
    }
}
