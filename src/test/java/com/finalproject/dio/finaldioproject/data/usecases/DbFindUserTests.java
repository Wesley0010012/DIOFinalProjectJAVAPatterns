package com.finalproject.dio.finaldioproject.data.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.data.protocols.FindUserRepository;
import com.finalproject.dio.finaldioproject.data.protocols.ModelMapperExecuter;
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

@TestComponent
public class DbFindUserTests {
    private static FindUserRepository findUserRepositoryStub = null;
    private static ModelMapperExecuter<UserModel, UserDTO> modelMapperStub = null;
    private static DbFindUser sut = null;

    @SuppressWarnings("unchecked")
    @BeforeAll
    public static void setUp() {
        findUserRepositoryStub = mock(FindUserRepository.class);
        modelMapperStub = mock(ModelMapperExecuter.class);
        sut = new DbFindUser(findUserRepositoryStub, modelMapperStub);
    }

    @Test
    @DisplayName("Should throw Error if ModelMapper throws")
    public void findUserRepositoryThrows() {

        when(modelMapperStub.convert(any(UserDTO.class))).thenThrow(new Error());

        assertThrows(Error.class, () -> sut.find(FakeUser.makeFakeUserDTO()));
    }
}
