package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseTransactionDto;

public interface ITransactionService {

    ResponseTransactionDto save(ResponseTransactionDto transactionDto);
}
