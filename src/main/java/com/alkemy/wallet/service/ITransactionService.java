package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseTransactionDto;

import java.util.List;

public interface ITransactionService {

    ResponseTransactionDto save(ResponseTransactionDto transactionDto);
    List<ResponseTransactionDto> findByUserId(Long userId);
}
