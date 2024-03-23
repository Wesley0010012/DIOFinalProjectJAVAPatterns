package com.finalproject.dio.finaldioproject;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import com.finalproject.dio.finaldioproject.presentation.controllers.AddUserController;

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
}
