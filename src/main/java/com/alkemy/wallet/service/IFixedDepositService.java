package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedDepositDto;
import com.alkemy.wallet.dto.ResponseSimulateFixedDepositDto;

public interface IFixedDepositService {
    String addFixedDeposit(String token, FixedDepositDto dto) throws Exception;
    ResponseSimulateFixedDepositDto simulateFixedDeposit(FixedDepositDto dto);
}
