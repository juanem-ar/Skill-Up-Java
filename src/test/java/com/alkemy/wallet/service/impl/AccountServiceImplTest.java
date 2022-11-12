package com.alkemy.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.List;
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
	
	@Mock
	private IFixedDepositService fixedDepositService;
	
	@Mock
	private IFixedTermDepositMapper fixedTermDepositMapper;

	@InjectMocks
	private AccountServiceImpl accountService;


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

		when(AccountRepository.findAllByUserId(userId))
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
