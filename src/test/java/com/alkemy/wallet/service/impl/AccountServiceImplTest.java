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

import com.alkemy.wallet.dto.AccountBalanceDto;
import com.alkemy.wallet.dto.ResponseUserBalanceDto;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.security.service.IJwtUtils;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.IUserService;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
	@Mock
	private IAccountRepository AccountRepository;

	@Mock
	private IUserService userService;

	@Mock
	private IJwtUtils jwtUtils;

	@Mock
	private ITransactionService transactionService;

	@Mock
	private IAccountMapper accountMapper;

	@InjectMocks
	private AccountServiceImpl accountService;


	@Test
	void getBalance_AccountWithThreeTransactions_ExactBalance() {
		String token = "token";
		Long userId = 1L;
		when(jwtUtils.getJwt(token)).thenReturn(token);
		when(jwtUtils.extractUserId(token)).thenReturn(userId);

		// three transactions
		List<Transaction> transactions = new ArrayList<>();

		Double incomeAmount = 1000.0;
		Transaction income = new Transaction();
		income.setType(EType.INCOME);
		income.setAmount(incomeAmount);
		transactions.add(income);

		Double paymentAmount = 100.0;
		Transaction payment = new Transaction();
		payment.setType(EType.PAYMENT);
		payment.setAmount(paymentAmount);
		transactions.add(payment);

		Double depositAmount = 10.0;
		Transaction deposit = new Transaction();
		deposit.setType(EType.DEPOSIT);
		deposit.setAmount(depositAmount);
		transactions.add(deposit);
		//

		Double balanceBase = 1.0;
		Long accountId = 1L;
		Account account = new Account();
		account.setId(accountId);
		account.setBalance(balanceBase);

		AccountBalanceDto balanceDto =
			new AccountBalanceDto(accountId, null, balanceBase);

		when(accountMapper.accountToBalanceDto(account))
			.thenReturn(balanceDto);

		when(AccountRepository.findAllByUserId(userId))
			.thenReturn(List.of(account));

		when(transactionService.findAllTransactionsWith(accountId))
			.thenReturn(transactions);

		Double balanceFinal =
			balanceBase + incomeAmount + depositAmount
				- paymentAmount;

		ResponseUserBalanceDto result =
			accountService.getBalance(token);

		assertEquals(1, result.getAccountBalanceDtos().size());

		double epsilon = 0.000001d;
		assertEquals(
			balanceFinal,
			result.getAccountBalanceDtos().get(0).getBalance(),
			epsilon);
	}

}
