package com.finalproject.dio.finaldioproject;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.ResponseEntity;

import com.finalproject.dio.finaldioproject.data.dto.UserDTO;
import com.finalproject.dio.finaldioproject.presentation.controllers.AddUserController;
import com.finalproject.dio.finaldioproject.presentation.errors.MissingParamError;
import com.finalproject.dio.finaldioproject.presentation.helpers.HttpHelpers;
import com.finalproject.dio.finaldioproject.presentation.protocols.HttpRequest;

@TestComponent
class AddUserControllerTests {

	private static AddUserController sut;

	@BeforeAll
	public static void setUp() {
		sut = new AddUserController();
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
}
