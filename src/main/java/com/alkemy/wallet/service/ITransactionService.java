package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.ResponseTransactionDto;

import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;

import com.alkemy.wallet.dto.TransactionDtoPay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ITransactionService {

    TransactionDtoPay sendArs(Long senderId, Long accountId, Double amount);
    ResponseTransactionDto save(ResponseTransactionDto transactionDto);

    List<Transaction> findAllTransactionsWith(Long accountId);

    ResponseTransactionDto payment(ResponseTransactionDto responseTransactionDto);
    Page<Transaction> findByUserId(Long userId, String token, Pageable pageable) throws Exception;
    Optional<ResponseTransactionDto> findTransactionById(Long id, String token) throws Exception;
    ResponseTransactionDto updateDescriptionFromTransaction(Long id, String token, String description) throws Exception;

}
