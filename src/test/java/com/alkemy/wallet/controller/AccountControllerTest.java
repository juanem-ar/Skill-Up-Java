package com.alkemy.wallet.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.alkemy.wallet.dto.ResponseUserBalanceDto;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { AccountController.class })
@ContextConfiguration(classes = { AccountController.class })
class AccountControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	private IUserService userService;

	@MockBean
	private IAccountService accountService;

	@MockBean
	private IAccountMapper iAccountMapper;

	private String uri = "/api/v1/accounts";


	@Test
	void getAccountsBalance_GetRequestWithSpecificName_ResponseUserBalanceDto()
		throws Exception {
		String token = "token";

		when(accountService.getBalance(token))
			.thenReturn(new ResponseUserBalanceDto());

		mockMvc
			.perform(
				get(uri + "/balance")
					.header("authorization", "Bearer " + token))
			.andExpect(status().isOk());
	}

}
