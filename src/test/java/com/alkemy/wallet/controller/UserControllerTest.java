package com.alkemy.wallet.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.impl.UserServiceImpl;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { UserController.class })
@ContextConfiguration(classes = { UserController.class })
class UserControllerTest {
	private String uri = "/api/v1/";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userService;


	@Test
	void findAllUsers_GetRequest_ResponseOk() throws Exception {
		when(userService.findAllUsers())
			.thenReturn(new ArrayList<ResponseUserDto>());
		
		mockMvc
			.perform(get(uri + "users"))
			.andExpect(status().isOk());
	}

}
