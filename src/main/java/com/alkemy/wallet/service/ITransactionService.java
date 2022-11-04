package com.alkemy.wallet.service;

import java.util.List;

import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;

public interface ITransactionService {

    String getJwt(String token);
    //ResponseTransactionDto sendArs(String token, Long accountId, Long amount, EType type); // faltan métodos llamados adentro
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);
    
    List<Transaction> findAllTransactionsWith(Account account);
}
