package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.dto.ResponseAccountsDto;
import com.alkemy.wallet.dto.ResponseUsersDto;
import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
class AccountServiceImplIntegrationTest {
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IAccountRepository accountRepository;
	@Autowired
	private IUserRepository userRepository;

	@Test
	@Transactional
	void findAllAccountsByUserId_DataBaseWithOneAccount_ReturnAListWithOneDto() throws ResourceNotFoundException {

		accountRepository.deleteAll();
		userRepository.deleteAll();

		User user1 = new User();

		user1.setFirstName("user");
		user1.setLastName("test");
		user1.setEmail("user@email.com");
		user1.setPassword("password");
		userRepository.save(user1);

		Account acc1 = new Account();

		Double transactionLimit = 1000.0;
		acc1.setUser(user1);
		acc1.setCurrency(ECurrency.USD);
		acc1.setBalance(0.0);
		acc1.setTransactionLimit(transactionLimit);
		acc1.setDeleted(false);
		accountRepository.save(acc1);
		
		List<ResponseAccountDto> result = accountService.findAllByUser(user1.getId());

		assertEquals(1, result.size());
		assertEquals(
			transactionLimit,
			result.get(0).getTransactionLimit());
	}

}
