package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.model.EType;

public interface ITransactionService {

    String getJwt(String token);
    //ResponseTransactionDto sendArs(String token, Long accountId, Long amount, EType type); // faltan métodos llamados adentro
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);
}
