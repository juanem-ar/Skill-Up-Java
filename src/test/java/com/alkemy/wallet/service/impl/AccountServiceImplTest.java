package com.alkemy.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.dto.UpdateAccountDto;
import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.model.*;
import com.alkemy.wallet.repository.IUserRepository;
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
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
	@Mock
	private IAccountRepository accountRepository;

	@Mock
	private IUserRepository userRepository;

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
	void updateAccount_UpdateAccountDto_ResponseAccountDto()
			throws ResourceNotFoundException {

		String token = "token";
		Long userId = 1L;
		Long accountId = 2L;
		Double transactionLimit = 9999.0 ;

		User user = new User();
		user.setEmail("123@mail.com");

		//create the target account to update
		Account account = new Account();
		account.setId(accountId);
		account.setTransactionLimit(1000.0);
		account.setUser(user);

		//create the request object
		UpdateAccountDto requestAccountDto = new UpdateAccountDto();
		requestAccountDto.setTransactionLimit(transactionLimit);

		//create the response object
		ResponseAccountDto responseAccountDto = new ResponseAccountDto();

		//mock the repository calls and validations
		when(jwtUtils.getJwt(any(String.class))).thenReturn(token);
		when(jwtUtils.extractUsername(any(String.class))).thenReturn(user.getEmail());
		when(accountRepository.save(any(Account.class))).thenReturn(account);
		when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(account));
		when(accountMapper.accountToAccountDto(any(Account.class))).thenReturn(responseAccountDto);

		//get the result
		ResponseAccountDto results = accountService.updateAccount(account.getId(),requestAccountDto,token);

		//assert results
		//assertEquals(requestAccountDto.getTransactionLimit(),responseAccountDto.getTransactionLimit());
		assertNotNull(results);
	}

	@Test
	void updateAccount_WithInvalidAccountId(){

		String token = "token";
		Long accountId = 520L; //account doesn't exists
		Double transactionLimit = 9999.0 ;

		User user = new User();
		user.setEmail("123@mail.com");

		UpdateAccountDto requestAccountDto = new UpdateAccountDto();
		requestAccountDto.setTransactionLimit(transactionLimit);

		//bypass the user validation
		when(jwtUtils.getJwt(any(String.class))).thenReturn(token);
		when(jwtUtils.extractUsername(any(String.class))).thenReturn("123@mail.com");

		assertThrows(ResourceNotFoundException.class,
				() -> accountService.updateAccount(accountId,requestAccountDto,token));

	}

	@Test
	void updateAccount_WithInvalidUser(){

		String token = "token";
		Long accountId = 1L;
		Long userId = 500L;
		Double transactionLimit = 9999.0 ;

		User user = new User();
		user.setId(userId);
		user.setEmail("123@mail.com");

		Account account = new Account();
		account.setId(accountId);
		account.setUser(user);

		UpdateAccountDto requestAccountDto = new UpdateAccountDto();
		requestAccountDto.setTransactionLimit(transactionLimit);

		when(jwtUtils.getJwt(any(String.class))).thenReturn(token);
		//the user logged in is different to the one making the request
		when(jwtUtils.extractUsername(any(String.class))).thenReturn("1234@mail.com");
		when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(account));

		assertThrows(AccessDeniedException.class,
				() -> accountService.updateAccount(accountId,requestAccountDto,token));

	}

	@Test
	void getBalance_AccountWithThreeTransactions_ExactBalance() {
		String token = "token";
		Long userId = 1L;
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

		when(accountRepository.findAllByUserId(userId))
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
