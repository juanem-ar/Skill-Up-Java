package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.model.Account;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IAccountService {
	ResponseUserBalanceDto getBalance(Authentication authentication);
    String addAccount(String email, String currency) throws Exception;
    Account createAccount(CurrencyDto currency);
    List<ResponseAccountDto> findAllByUser(Long id);
    ResponseAccountsListDto findAll(Integer page, HttpServletRequest httpServletRequest) throws BadRequestException, Exception;
    ResponseAccountDto updateAccount(Long id, Double limit, Authentication authentication) throws Exception;
}
