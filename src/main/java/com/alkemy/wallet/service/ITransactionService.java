package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.dto.TransactionDtoPay;
import java.util.List;
import java.util.Optional;

public interface ITransactionService {

    String getJwt(String token);
    //ResponseTransactionDto sendArs(String token, Long accountId, Long amount, EType type); // faltan métodos llamados adentro
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);
    public TransactionDtoPay payment(TransactionDtoPay transitionDtoPay);
    List<ResponseTransactionDto> findByUserId(Long userId);
    Optional<ResponseTransactionDto> findTransactionById(Long id);

}
