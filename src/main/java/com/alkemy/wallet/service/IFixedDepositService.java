package com.alkemy.wallet.service;

import java.util.List;
import com.alkemy.wallet.dto.ResponseSimulatedFixedDepositDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.FixedTermDeposit;
import org.springframework.security.core.Authentication;

public interface IFixedDepositService {
    String addFixedDeposit(Authentication authentication,Double amount, String currency, int period) throws Exception;
    ResponseSimulatedFixedDepositDto simulateFixedDeposit(String currency, int period, Double amount) throws Exception;
    List<FixedTermDeposit> findAllBy(Account account);

}
