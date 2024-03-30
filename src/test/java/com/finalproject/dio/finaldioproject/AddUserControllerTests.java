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

import com.finalproject.dio.finaldioproject.data.dto.CepDTO;
import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.domain.models.CepModel;
import com.finalproject.dio.finaldioproject.domain.models.UserModel;
import com.finalproject.dio.finaldioproject.domain.usecases.AddUser;
import com.finalproject.dio.finaldioproject.domain.usecases.FindUser;
import com.finalproject.dio.finaldioproject.presentation.controllers.AddUserController;
import com.finalproject.dio.finaldioproject.presentation.errors.CepNotFoundedError;
import com.finalproject.dio.finaldioproject.presentation.errors.InvalidParamError;
import com.finalproject.dio.finaldioproject.presentation.errors.MissingParamError;
import com.finalproject.dio.finaldioproject.presentation.errors.UserExistsError;
import com.finalproject.dio.finaldioproject.presentation.helpers.HttpHelpers;
import com.finalproject.dio.finaldioproject.presentation.protocols.CepValidator;
import com.finalproject.dio.finaldioproject.presentation.protocols.EmailValidator;
import com.finalproject.dio.finaldioproject.presentation.protocols.FindCepProxy;
import com.finalproject.dio.finaldioproject.presentation.protocols.HttpRequest;
import com.finalproject.dio.finaldioproject.presentation.protocols.JsonConverter;
import com.finalproject.dio.finaldioproject.presentation.protocols.NameValidator;

class FakeUser {
	public static UserModel makeFakeUser() {
		UserModel result = new UserModel();
		result.setId(1L);
		result.setName("any_name");
		result.setEmail("any_email");

		CepModel cep = new CepModel();
		cep.setId(1L);
		cep.setCode("any_code");
		cep.setCity("any_city");
		cep.setState("any_state");
		result.setCep(cep);

		return result;
	}

	public static UserModel makeFakeValidUser() {
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

	public static UserModel makeEmptyUser() {
		return new UserModel();
	}
}

class FakeCep {
	public static CepDTO makeFakeCep() {
		CepDTO cep = new CepDTO();
		cep.setCode("any_code");
		cep.setCity("any_city");
		cep.setState("any_state");

		return cep;
	}

	public static CepDTO makeEmptyCep() {
		return null;
	}
}

class FakeJson {
	public static String makeFakeJson() {
		return "{\"id\": \"1\",\"name\": \"valid_name\",\"email\": \"valid_email\",\"cep\": {\"id\": 1,\"code\": \"valid_code\",\"city\": \"valid_city\",\"state\": \"valid_state\"}}";
	}
}

@TestComponent
class AddUserControllerTests {

	private static NameValidator nameValidatorStub = null;
	private static AddUserController sut = null;
	private static EmailValidator emailValidatorStub = null;
	private static CepValidator cepValidatorStub = null;
	private static FindUser findUserStub = null;
	private static FindCepProxy findCepProxyStub = null;
	private static AddUser addUserStub = null;
	private static JsonConverter<UserModel> jsonConverter = null;

	@SuppressWarnings("unchecked")
	@BeforeAll
	public static void setUp() {
		nameValidatorStub = mock(NameValidator.class);
		emailValidatorStub = mock(EmailValidator.class);
		cepValidatorStub = mock(CepValidator.class);
		findUserStub = mock(FindUser.class);
		findCepProxyStub = mock(FindCepProxy.class);
		addUserStub = mock(AddUser.class);
		jsonConverter = mock(JsonConverter.class);
		
		sut = new AddUserController(nameValidatorStub, emailValidatorStub, cepValidatorStub, findUserStub, findCepProxyStub, addUserStub, jsonConverter);
	}

	@BeforeEach
	public void resetMocks() {
		when(nameValidatorStub.isValid(any(String.class))).thenReturn(true);
		when(emailValidatorStub.isValid(any(String.class))).thenReturn(true);
		when(cepValidatorStub.isValid(any(String.class))).thenReturn(true);
		when(findUserStub.find(any(UserDTO.class))).thenReturn(FakeUser.makeEmptyUser());
		when(findCepProxyStub.find(any(String.class))).thenReturn(FakeCep.makeFakeCep());
		when(addUserStub.add(any(UserDTO.class), any(CepDTO.class))).thenReturn(FakeUser.makeFakeValidUser());
		when(jsonConverter.convert(any(UserModel.class))).thenReturn(FakeJson.makeFakeJson());
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

	@Test
	@DisplayName("Should return 500 if FindUser throws")
	void findUserThrows() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("any_name");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(findUserStub.find(any(UserDTO.class))).thenThrow(new Error());
		
		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.internalServerError();

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 400 if cep not founded")
	void cepNotFounded() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("any_name");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(findCepProxyStub.find(any(String.class))).thenReturn(FakeCep.makeEmptyCep());

		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.badRequest(new CepNotFoundedError(body.getCep()));

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 500 if FindCepProxy throws")
	void findCepProxyThrows() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("any_name");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(findCepProxyStub.find(any(String.class))).thenThrow(new Error());

		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.internalServerError();

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 500 if AddUser throws")
	void addUserThrows() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("any_name");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(addUserStub.add(any(UserDTO.class), any(CepDTO.class))).thenThrow(new Error());

		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.internalServerError();

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return 500 if JsonConverter throws")
	void jsonConverterThrows() {
		UserDTO body = new UserDTO();
		body.setName("any_name");
		body.setEmail("any_name");
		body.setCep("any_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		when(jsonConverter.convert(any(UserModel.class))).thenThrow(new Error());

		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.internalServerError();

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}

	@Test
	@DisplayName("Should return an Account on success")
	void success() {
		UserDTO body = new UserDTO();
		body.setName("valid_name");
		body.setEmail("valid_email");
		body.setCep("valid_cep");

		HttpRequest<UserDTO> httpRequest = new HttpRequest<UserDTO>();
		httpRequest.setBody(body);

		ResponseEntity<String> httpResponse = sut.handle(httpRequest);

		ResponseEntity<String> sample = HttpHelpers.success(FakeJson.makeFakeJson());

		assertEquals(sample.getStatusCode(), httpResponse.getStatusCode());
		assertEquals(sample.getBody(), httpResponse.getBody());
	}
}
