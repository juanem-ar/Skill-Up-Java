package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseUserBalanceDto;
import com.alkemy.wallet.dto.CurrencyDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;

import java.util.List;

public interface IAccountService {
    List<Account> findAllByUser(User user);

	ResponseUserBalanceDto getBalance(String token);
	
    void addAccount(String email, CurrencyDto currency) throws Exception;
    Account createAccount(CurrencyDto currency);
}
