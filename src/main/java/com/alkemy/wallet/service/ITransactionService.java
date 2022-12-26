package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.Transaction;
import java.util.List;

public interface ITransactionService {
    ResponseTransactionDto send(String token, ResponseSendTransactionDto responseSendTransactionDto, ECurrency currency) throws Exception;
    ResponseTransactionDto save(RequestTransactionDto transactionDto, String token);
    List<Transaction> findAllTransactionsWith(Long accountId);
    List<ResponseTransactionDto> findAllTransactionsByUserId(String token);
    TransactionPageDto findAllByAccount(Integer page) throws Exception;
    ResponseTransactionDto payment(TransactionDtoPay transactionDtoPay) throws Exception;
    ResponseTransactionDto findResponseTransactionById(Long id, String token) throws Exception;
    Transaction findTransactionById(Long id, String token) throws Exception;
    ResponseTransactionDto updateDescriptionFromTransaction(Long id, String token, String description) throws Exception;

}
