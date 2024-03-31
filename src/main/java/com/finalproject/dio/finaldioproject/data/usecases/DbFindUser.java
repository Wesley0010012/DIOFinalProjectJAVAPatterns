package com.finalproject.dio.finaldioproject.data.usecases;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.data.protocols.FindUserRepository;
import com.finalproject.dio.finaldioproject.data.protocols.ModelMapperExecuter;
import com.finalproject.dio.finaldioproject.domain.models.UserModel;
import com.finalproject.dio.finaldioproject.domain.usecases.FindUser;

public class DbFindUser implements FindUser {

    private FindUserRepository findUserRepository = null;
    private ModelMapperExecuter<UserModel, UserDTO> modelMapperExecuter = null;

    public DbFindUser(FindUserRepository findUserRepository, ModelMapperExecuter<UserModel, UserDTO> modelMapper) {
        this.modelMapperExecuter = modelMapper;
        this.findUserRepository = findUserRepository;
    }

    @Override
    public UserModel find(UserDTO user) {
        this.modelMapperExecuter.convert(user);

        return new UserModel();
    }
    
}
