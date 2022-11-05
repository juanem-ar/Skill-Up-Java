package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.model.EType;

public interface ITransactionService {

    String getJwt(String token);
    //ResponseTransactionDto sendArs(String token, Long accountId, Double amount) // faltan métodos llamados dentro
    //ResponseTransactionDto sendUsd(String token, Long accountId, Double amount) // faltan métodos llamados dentro
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);
}
