package com.finalproject.dio.finaldioproject.data.usecases;

import com.finalproject.dio.finaldioproject.data.dto.CepDTO;
import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.data.protocols.AddUserRepository;
import com.finalproject.dio.finaldioproject.data.protocols.ModelMapperExecuter;
import com.finalproject.dio.finaldioproject.domain.models.CepModel;
import com.finalproject.dio.finaldioproject.domain.models.UserModel;
import com.finalproject.dio.finaldioproject.domain.usecases.AddUser;

public class DbAddUser implements AddUser {

    private AddUserRepository userRepository;
    private ModelMapperExecuter<UserModel, UserDTO> userModelMapperExecuter = null;
    private ModelMapperExecuter<CepModel, CepDTO> cepModelMapperExecuter = null;

    public DbAddUser(AddUserRepository userRepository, ModelMapperExecuter<UserModel, UserDTO> userModelMapperExecuter, ModelMapperExecuter<CepModel, CepDTO> cepModelMapperExecuter) {
        this.userRepository = userRepository;
        this.userModelMapperExecuter = userModelMapperExecuter;
        this.cepModelMapperExecuter = cepModelMapperExecuter;
    }

    @Override
    public UserModel add(UserDTO user, CepDTO cep) {
        UserModel userModel = this.userModelMapperExecuter.convert(user);

        this.cepModelMapperExecuter.convert(cep);

        return new UserModel();
    }
    
}
