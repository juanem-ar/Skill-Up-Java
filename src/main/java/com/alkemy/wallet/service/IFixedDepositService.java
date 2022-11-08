package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedDepositDto;

public interface IFixedDepositService {
    String addFixedDeposit(String token, FixedDepositDto dto) throws Exception;
}
