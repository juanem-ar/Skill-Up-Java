package com.alkemy.wallet.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import com.alkemy.wallet.dto.ResponseUserListDto;
import com.alkemy.wallet.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@WebMvcTest(controllers = { UserController.class })
@ContextConfiguration(classes = { UserController.class })
class UserControllerTest {
	private String uri = "/users";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userService;

	private ObjectMapper objectMapper = new ObjectMapper();


	@Test
	@WithMockUser(roles = {"ADMIN"})
	void findAllUsers_GetRequestWithoutRequestParameter_ResponseOk() throws Exception {
		when(userService.findAllUsers(any(), any()))
			.thenReturn(new ResponseUserListDto());
		
		mockMvc
			.perform(get(uri))
			.andExpect(status().isOk());
	}
	
	@Test
	void findAllUsers_GetRequestWithoutRequestParameter_ResponseUnauthorized() throws Exception {
	  when(userService.findAllUsers(any(), any()))
	    .thenReturn(new ResponseUserListDto());
	  
	  mockMvc.perform(get(uri)).andExpect(status().isUnauthorized());
	  }


	@Test
	@WithMockUser(roles = {"ADMIN"})
	void findAllUsers_GetRequestWithRequestParameter_ResponseOk() throws Exception {
		when(userService.findAllUsers(any(), any()))
			.thenReturn(new ResponseUserListDto());
		
		Integer pageNumber = 1;
		
		mockMvc
			.perform(get(uri)
				.param("page", pageNumber.toString()))
			.andExpect(status().isOk());
	}


	@Test
	@WithMockUser(roles = {"USER"})
	void getUserDetails_GetRequestWithUriWithVariable_ResponseOk()
		throws Exception {
		Long userId = 1L;
		String token = "token";

		mockMvc
			.perform(
				get(uri + "/" + userId.toString())
					.header("authorization", "Bearer " + token))
			.andExpect(status().isOk());
	}


	@Test
    @WithMockUser(roles = {"USER"})
	void updateUserDetails_PatchRequestWithBodyAndUriWithId_ResponseOk()
		throws Exception {
		Long userId = 1L;
		String token = "token";
		ResponseDetailsUserDto dto = new ResponseDetailsUserDto();

		when(userService.updateUserDetails(any(), any(), any()))
			.thenReturn(dto);

		PatchRequestUserDto requestDto = new PatchRequestUserDto();
		requestDto.setFirstName("name");
		requestDto.setLastName("name");
		
		String body =
			objectMapper
				.writeValueAsString(requestDto);

		mockMvc
			.perform(
				patch(uri + "/" + userId.toString())
				    .with(csrf())
					.content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.header("authorization", "Bearer " + token))
			.andExpect(status().isOk());

	}

}
