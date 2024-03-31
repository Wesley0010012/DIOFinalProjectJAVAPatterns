package com.finalproject.dio.finaldioproject.data.usecases;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.data.protocols.FindUserRepository;
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

    public static UserModel makeFakeUser() {
        UserModel result = new UserModel();
		result.setId(1L);
		result.setName("valid_name");
		result.setEmail("valid_email");

		CepModel cep = new CepModel();
		cep.setId(1L);
		cep.setCode("valid_code");
		cep.setCity("valid_city");
		cep.setState("valid_state");
		result.setCep(cep);

		return result;
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

    @BeforeEach
    public void resetMocks() {
        when(modelMapperStub.convert(any(UserDTO.class))).thenReturn(FakeUser.makeFakeUser());
    }

    @Test
    @DisplayName("Should call ModelMapperExecuter with correct UserDTO")
    public void modelMapperExecuterThrows() {

        when(modelMapperStub.convert(any(UserDTO.class))).thenThrow(new Error());

        assertThrows(Error.class, () -> sut.find(FakeUser.makeFakeUserDTO()));
    }


    @Test
    @DisplayName("Should throw Error if AddUserRepository throws")
    public void findUserRepositoryThrows() {

        when(findUserRepositoryStub.find(any(UserModel.class))).thenThrow(new Error());

        assertThrows(Error.class, () -> sut.find(FakeUser.makeFakeUserDTO()));
    }

    @Test
    @DisplayName("Should return an Empty object if user not founded")
    public void userNotFounded() {

        when(findUserRepositoryStub.find(any(UserModel.class))).thenReturn(null);

        UserModel result = this.sut.find(FakeUser.makeFakeUserDTO());

        assertSame(null, result);
    }
}
