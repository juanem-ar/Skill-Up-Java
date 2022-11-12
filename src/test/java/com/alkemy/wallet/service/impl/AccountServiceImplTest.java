package com.alkemy.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
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
import com.alkemy.wallet.dto.ResponseFixedDepositDto;
import com.alkemy.wallet.dto.ResponseUserBalanceDto;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.mapper.IFixedTermDepositMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.security.service.IJwtUtils;
import com.alkemy.wallet.service.IFixedDepositService;
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

	@Mock
	private IFixedDepositService fixedDepositService;

	@Mock
	private IFixedTermDepositMapper fixedTermDepositMapper;

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
		account.setCurrency(ECurrency.USD);
		account.setCreationDate(LocalDateTime.now());
		account.setUpdateDate(LocalDateTime.now());
		account.setBalance(0.0);
		account.setDeleted(false);

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
		assertNotNull(results);
		//assertEquals(requestAccountDto.getTransactionLimit(),responseAccountDto.getTransactionLimit());

	}

	@Test
	void updateAccount_WithInvalidAccountId_ResourceNotFoundException(){

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
	void updateAccount_WithInvalidUser_AccessDeniedException(){

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
		when(jwtUtils.getJwt(token)).thenReturn(token);
		when(jwtUtils.extractUserId(token)).thenReturn(userId);

		Double ammountDeposit = 1.0;
		FixedTermDeposit fixedTermDeposit = new FixedTermDeposit();
		fixedTermDeposit.setAmount(ammountDeposit);
		ResponseFixedDepositDto depositDto = new ResponseFixedDepositDto();
		depositDto.setAmount(ammountDeposit);

		Double balanceBase = 1000.0;
		Long accountId = 1L;
		Account account = new Account();
		account.setId(accountId);
		account.setBalance(balanceBase);

		AccountBalanceDto balanceDto =
			new AccountBalanceDto(accountId, null, balanceBase);

		when(accountRepository.findAllByUserId(userId))
          .thenReturn(List.of(account));

		when(accountMapper.accountToBalanceDto(account))
			.thenReturn(balanceDto);

		when(fixedDepositService.findAllBy(account))
		  .thenReturn(List.of(fixedTermDeposit));

		when(fixedTermDepositMapper.toResponseFixedDepositDto(fixedTermDeposit))
		  .thenReturn(depositDto);

		Double balanceFinal = balanceBase - ammountDeposit;

		ResponseUserBalanceDto result =
			accountService.getBalance(token);

		assertEquals(1, result.getAccountBalanceDtos().size());

		double epsilon = 0.000001d;
		assertEquals(
			balanceFinal,
			result.getAccountBalanceDtos().get(0).getBalance(),
			epsilon);

		assertEquals(1, result.getDepositDtos().size());
	}

}
