package com.finalproject.dio.finaldioproject.data.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.finalproject.dio.finaldioproject.data.dto.CepDTO;
import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.data.protocols.AddUserRepository;
import com.finalproject.dio.finaldioproject.data.protocols.ModelMapperExecuter;
import com.finalproject.dio.finaldioproject.domain.models.CepModel;
import com.finalproject.dio.finaldioproject.domain.models.UserModel;

class FakeUser {
    public static UserDTO makeFakeUserDTO() {
        UserDTO user = new UserDTO();
        user.setName("any_name");
        user.setEmail("any_email");
        user.setCep("any_cep");

        return user;
    }
}

class FakeCep {
    public static CepDTO makeFakeCepDTO() {
        CepDTO cep = new CepDTO();
        cep.setCode("any_code");
        cep.setCity("any_city");
        cep.setState("any_state");

        return cep;
    }
}


public class DbAddUserTests {
    private static ModelMapperExecuter<UserModel, UserDTO> userModelMapperExecuterStub = null;
    private static ModelMapperExecuter<CepModel,CepDTO> cepModelMapperExecuterStub = null;
    private static AddUserRepository userRepositoryStub = null;
    private static DbAddUser sut = null;

    @SuppressWarnings("unchecked")
    @BeforeAll
    public static void setUp() {
        userModelMapperExecuterStub = mock(ModelMapperExecuter.class);
        cepModelMapperExecuterStub = mock(ModelMapperExecuter.class);
        userRepositoryStub = mock(AddUserRepository.class);
        sut = new DbAddUser(userRepositoryStub, userModelMapperExecuterStub, cepModelMapperExecuterStub);
    }

    @Test
    @DisplayName("Should throw if UserModelMapperExecuter throws")
    public void userModelMapperExecuterThrows() {
        when(userModelMapperExecuterStub.convert(any(UserDTO.class))).thenThrow(new Error());

        assertThrows(Error.class, () -> sut.add(FakeUser.makeFakeUserDTO(), FakeCep.makeFakeCepDTO()));
    }
}
