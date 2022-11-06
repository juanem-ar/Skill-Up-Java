package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseUserBalanceDto;
import com.alkemy.wallet.dto.CurrencyDto;
import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.model.Account;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IAccountService {
	ResponseUserBalanceDto getBalance(String token);

    void addAccount(String email, CurrencyDto currency) throws Exception;

    Account createAccount(CurrencyDto currency);

    List<ResponseAccountDto> findAllByUser(Long id);

    Optional<Account> findById(Long id);

    ResponseAccountDto updateAccount(Account account, Map<String,Double> transactionLimit, Authentication authentication);
}
