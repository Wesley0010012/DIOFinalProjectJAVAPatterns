package com.finalproject.dio.finaldioproject;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.ResponseEntity;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.domain.models.CepModel;
import com.finalproject.dio.finaldioproject.domain.models.UserModel;
import com.finalproject.dio.finaldioproject.domain.usecases.FindUser;
import com.finalproject.dio.finaldioproject.presentation.controllers.AddUserController;
import com.finalproject.dio.finaldioproject.presentation.errors.InvalidParamError;
import com.finalproject.dio.finaldioproject.presentation.errors.MissingParamError;
import com.finalproject.dio.finaldioproject.presentation.errors.UserExistsError;
import com.finalproject.dio.finaldioproject.presentation.helpers.HttpHelpers;
import com.finalproject.dio.finaldioproject.presentation.protocols.CepValidator;
import com.finalproject.dio.finaldioproject.presentation.protocols.EmailValidator;
import com.finalproject.dio.finaldioproject.presentation.protocols.HttpRequest;
import com.finalproject.dio.finaldioproject.presentation.protocols.NameValidator;

class FakeUser {
	public static UserModel makeFakeUser() {
		UserModel result = new UserModel();
		result.setId(1L);
		result.setName("any_name");
		result.setEmail("any_email");

		CepModel cep = new CepModel();
		cep.setCode("any_code");
		cep.setCity("any_city");
		cep.setState("any_state");
		result.setCep(cep);

		return result;
	}
}

@TestComponent
class AddUserControllerTests {

	private static NameValidator nameValidatorStub = null;
	private static AddUserController sut = null;
	private static EmailValidator emailValidatorStub = null;
	private static CepValidator cepValidatorStub = null;
	private static FindUser findUserStub = null;

	@BeforeAll
	public static void setUp() {
		nameValidatorStub = mock(NameValidator.class);
		emailValidatorStub = mock(EmailValidator.class);
		cepValidatorStub = mock(CepValidator.class);
		findUserStub = mock(FindUser.class);
		
		sut = new AddUserController(nameValidatorStub, emailValidatorStub, cepValidatorStub, findUserStub);
	}

	@BeforeEach
	public void resetMocks() {
		when(nameValidatorStub.isValid(any(String.class))).thenReturn(true);
		when(emailValidatorStub.isValid(any(String.class))).thenReturn(true);
		when(cepValidatorStub.isValid(any(String.class))).thenReturn(true);
	}

	@Test
	@DisplayName("Initial Test Load")
	void contextLoads() throws Exception {
		assertThat(sut).isNotNull();
	}

    @Test
	@DisplayName("Should return 400 if no name was provided")
	void noNameWasProvided() {
		UserDTO body = new UserDTO();
		body.setName(null);
		body.setEmail("any_email");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.badRequest(new MissingParamError("name"));

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

    @Test
	@DisplayName("Should return 400 if no email was provided")
	void noEmailWasProvided() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.badRequest(new MissingParamError("email"));

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 400 if no cep was provided")
	void noCepWasProvided() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("any_email");
		body.setCep("");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.badRequest(new MissingParamError("cep"));

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 400 if invalid name was provided")
	void invalidNameProvided() {
		UserDTO body = new UserDTO();
		body.setName("invalid_name");
		body.setEmail("any_email");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(nameValidatorStub.isValid(any(String.class))).thenReturn(false);
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.badRequest(new InvalidParamError("name"));

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 500 if NameValidator throws")
	void nameValidatorThrows() {
		UserDTO body = new UserDTO();
		body.setName("invalid_name");
		body.setEmail("any_email");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(nameValidatorStub.isValid(body.getName())).thenThrow(new Error());
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.internalServerError();

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 400 if invalid email is provided")
	void invalidEmailProvided() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("invalid_email");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(emailValidatorStub.isValid(body.getEmail())).thenReturn(false);
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.badRequest(new InvalidParamError("email"));

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 500 if EmailValidator throws")
	void emailValidatorThrows() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("invalid_email");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(emailValidatorStub.isValid(body.getEmail())).thenThrow(new Error());
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.internalServerError();

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 400 if invalid cep is provided")
	void invalidCepProvided() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("any_name");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);


		when(cepValidatorStub.isValid(body.getCep())).thenReturn(false);
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.badRequest(new InvalidParamError("cep"));

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 500 if CepValidator throws")
	void cepValidatorThrows() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("any_name");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(cepValidatorStub.isValid(body.getCep())).thenThrow(new Error());
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.internalServerError();
		
		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 400 if user exists")
	void userExists() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("any_name");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(findUserStub.find(any(UserDTO.class))).thenReturn(FakeUser.makeFakeUser());
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.badRequest(new UserExistsError(body));

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}
}
