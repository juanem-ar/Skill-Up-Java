package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.model.EType;

import java.util.List;

public interface ITransactionService {

    String getJwt(String token);
    //ResponseTransactionDto sendArs(String token, Long accountId, Long amount, EType type); // faltan métodos llamados adentro
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);
    List<ResponseTransactionDto> findByUserId(Long userId);
}
