package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.ResponseTransactionDto;

import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;

import com.alkemy.wallet.dto.TransactionDtoPay;
import java.util.List;
import java.util.Optional;


public interface ITransactionService {

    String getJwt(String token);
    //ResponseTransactionDto sendArs(String token, Long accountId, Double amount) // faltan métodos llamados dentro
    //ResponseTransactionDto sendUsd(String token, Long accountId, Double amount) // faltan métodos llamados dentro
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);

    List<Transaction> findAllTransactionsWith(Long accountId);

    public TransactionDtoPay payment(TransactionDtoPay transitionDtoPay);
    List<ResponseTransactionDto> findByUserId(Long userId);

    Optional<ResponseTransactionDto> findTransactionById(Long id);
    ResponseTransactionDto updateDescriptionFromTransaction(ResponseTransactionDto responseTransactionDto, String description);

}
