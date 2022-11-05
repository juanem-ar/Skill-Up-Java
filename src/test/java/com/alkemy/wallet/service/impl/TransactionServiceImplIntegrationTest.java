package com.alkemy.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.Account.Currency;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.ITransactionService;

@SpringBootTest
@ActiveProfiles(profiles = { "test" })
class TransactionServiceImplIntegrationTest {
	@Autowired
	private ITransactionService transactionService;

	@Autowired
	private ITransactionRepository transactionRepository;

	@Autowired
	private IAccountRepository accountRepository;

	@Autowired
	private IUserRepository userRepository;


	@Test
	void findAllTransactionsWith_AccountWithTwoTranstransaction_ReturnTwoTranstransaction() {
		User user = new User();
		user.setFirstName("firstname");
		user.setLastName("lastname");
		user.setEmail("user@email.com");
		user.setPassword("password");
		user = userRepository.save(user);

		Account account = new Account();
		account.setCurrency(Currency.ARS);
		account.setTransactionLimit(100000.0);
		account.setBalance(0.0);
		account.setUser(user);
		// problema con los models
		account.setUserId(user.getId());
		//
		account = accountRepository.save(account);

		Transaction transaction1 = new Transaction();
		transaction1.setAmount(10.0);
		transaction1.setType(EType.DEPOSIT);
		transaction1.setAccount(account);
		// problema con los models
		//transaction1.setAccountId(account.getId());
		//
		transaction1 = transactionRepository.save(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setAmount(10.0);
		transaction2.setType(EType.DEPOSIT);
		transaction2.setAccount(account);
		// problema con los models
		//transaction2.setAccountId(account.getId());
		//
		transaction2 = transactionRepository.save(transaction2);

		List<Transaction> result =
			transactionService.findAllTransactionsWith(account);

		assertEquals(2, result.size());
	}

}
