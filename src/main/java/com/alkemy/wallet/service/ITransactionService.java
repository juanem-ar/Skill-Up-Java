package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.service.generic.GenericServiceAPI;

public interface ITransactionService extends GenericServiceAPI<Transaction, Long> {

    String getJwt(String token);
    //TransactionDto sendArs(String token, Long accountId, Long amount, EType type);
    Transaction save(Transaction entity);
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);
}
