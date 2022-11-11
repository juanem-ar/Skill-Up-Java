package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.ResponseSendTransactionDto;
import com.alkemy.wallet.dto.ResponseTransactionDto;

import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.exceptions.TransactionError;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ITransactionService {

    ResponseTransactionDto send(Long senderId, ResponseSendTransactionDto responseSendTransactionDto, ECurrency currency) throws TransactionError;
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);

    List<Transaction> findAllTransactionsWith(Long accountId);

    ResponseTransactionDto payment(TransactionDtoPay transactionDtoPay);
    Page<Transaction> findByUserId(Long userId, String token, Pageable pageable) throws Exception;
    Optional<ResponseTransactionDto> findTransactionById(Long id, String token) throws Exception;
    ResponseTransactionDto updateDescriptionFromTransaction(Long id, String token, String description) throws Exception;

}
