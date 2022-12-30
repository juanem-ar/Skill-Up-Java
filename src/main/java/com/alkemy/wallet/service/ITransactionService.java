package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exceptions.TransactionError;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.Transaction;
import org.springframework.security.core.Authentication;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITransactionService {
    ResponseTransactionDto send(Authentication authentication, Double amount, String description, Long accountId,ECurrency currency) throws Exception;
    ResponseTransactionDto save(Double amount, String description, Long accountId, Authentication authentication) throws TransactionError, Exception;
    List<Transaction> findAllTransactionsWith(Long accountId);
    List<ResponseTransactionDto> findAllTransactionsByUserId(Authentication authentication) throws TransactionError, Exception;
    TransactionPageDto findAllByAccount(Integer page, HttpServletRequest httpServletRequest) throws Exception;
    ResponseTransactionDto payment(TransactionDtoPay transactionDtoPay) throws Exception;
    ResponseTransactionDto findResponseTransactionById(Long id, Authentication authentication) throws Exception;
    Transaction findTransactionById(Long id, Authentication authentication) throws Exception;
    ResponseTransactionDto updateDescriptionFromTransaction(Long id, Authentication authentication, String description) throws Exception;

}
