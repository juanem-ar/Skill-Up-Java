package com.alkemy.wallet.service;

import java.util.List;
import com.alkemy.wallet.dto.FixedDepositDto;
import com.alkemy.wallet.dto.ResponseSimulateFixedDepositDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.FixedTermDeposit;

public interface IFixedDepositService {
    String addFixedDeposit(String token, FixedDepositDto dto) throws Exception;
    ResponseSimulateFixedDepositDto simulateFixedDeposit(FixedDepositDto dto);
    List<FixedTermDeposit> findAllBy(Account accound);
}
