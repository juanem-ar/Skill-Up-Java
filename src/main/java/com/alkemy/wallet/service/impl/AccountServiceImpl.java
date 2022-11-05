package com.alkemy.wallet.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkemy.wallet.dto.AccountBalanceDto;
import com.alkemy.wallet.dto.ResponseUserBalanceDto;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.IUserService;
@Service
public class AccountServiceImpl implements IAccountService {
    private IAccountRepository iAccountRepository;
    
    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private ITransactionService transactionService;
    
    @Autowired
    private IAccountMapper accountMapper;
    
    @Autowired
    public AccountServiceImpl(IAccountRepository iAccountRepository) {
        this.iAccountRepository = iAccountRepository;
    }

    @Override
    public List<Account> findAllByUser(User user) {
        return user.getAccounts();
    }

	@Override
	public ResponseUserBalanceDto getBalance(String token) {
		Long userId = jwtUtils.extractUserId(token);
		
		User user = userService.getUserById(userId);
		
		ResponseUserBalanceDto responseUserBalanceDto = new ResponseUserBalanceDto();
		responseUserBalanceDto.setId(userId);
		
		for(Account account : user.getAccounts()) {
			AccountBalanceDto accountBalanceDto = accountMapper.accountToBalanceDto(account);
			
			accountBalanceDto.setBalance(
				calcularBalance(
					account.getBalance(),
					transactionService.findAllTransactionsWith(account)));

			responseUserBalanceDto.getAccountBalanceDtos().add(accountBalanceDto);
		}
		
		return responseUserBalanceDto;
	}

	
	private Double calcularBalance(
		Double balanceBase, List<Transaction> transactions) {
		Double b = balanceBase;
		
		for(Transaction transaction : transactions) {
			if(transaction.getType() == EType.DEPOSIT
				|| transaction.getType() == EType.INCOME)
				b += transaction.getAmount();
			else if(transaction.getType() == EType.PAYMENT)
				b -= transaction.getAmount();
		}
		
		return b;
	}
}
