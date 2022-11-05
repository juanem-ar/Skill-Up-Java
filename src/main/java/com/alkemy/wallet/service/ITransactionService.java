package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.dto.TransactionDtoPay;
import java.util.List;

public interface ITransactionService {

    String getJwt(String token);
    //ResponseTransactionDto sendArs(String token, Long accountId, Long amount, EType type); // faltan métodos llamados adentro
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);
    public TransactionDtoPay payment(TransactionDtoPay transitionDtoPay);
    List<ResponseTransactionDto> findByUserId(Long userId);

}
