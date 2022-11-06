package com.alkemy.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alkemy.wallet.mapper.ITransactionMapper;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
	@Mock
	private ITransactionRepository transactionRepository;

	@Mock
	private IUserRepository userRepository;

	@Mock
	private IAccountRepository accountRepository;

	@Mock
	private ITransactionMapper transactionMapper;

	@InjectMocks
	private TransactionServiceImpl transactionService;


	@Test
	void findAllTransactionsWith_AccountWithTwoTranstransaction_ReturnTwoTranstransaction() {
		Long accountId = 1L;

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction());
		transactions.add(new Transaction());

		when(transactionService.findAllTransactionsWith(accountId))
			.thenReturn(transactions);

		List<Transaction> result =
			transactionService.findAllTransactionsWith(accountId);
		
		assertEquals(2, result.size());
	}

}
