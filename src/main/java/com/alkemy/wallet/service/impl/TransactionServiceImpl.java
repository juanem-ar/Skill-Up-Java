package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.generic.GenericServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl extends GenericServiceImpl<Transaction, Long> implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final IUserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    private JwtUtils jwtUtils;

    public String getJwt(String token){
        String jwt = token.substring(7);
        return jwt;
    }

    /*
    public TransactionDto sendArs(String token, Long accountId, Long amount, EType type) {

        Long senderId = jwtUtils.extractUserId(getJwt(token));
        TransactionDto transaction = null;
        Account senderAccount = accountRepository.findArsAccountByUserId(senderId);
        Long receiverId = userRepository.findAccountByUserId(accountId);

        if (amount <= senderAccount.getBalance() && amount <= senderAccount.getTransactionLimit()) {

            transaction = transactionMapper.toDto(payment(senderId, receiverId, amount, type));
            income(accountId, receiverId, amount, EType.INCOME);
            log.info("Successful ARS transaction");

            save(transaction);
        } else {
            log.error("ARS transaction failed");
        }
        return transaction;
    }*/

    @Override
    public Transaction save(Transaction entity) {
        return transactionRepository.save(entity);
    }

    @Override
    public JpaRepository<Transaction, Long> getRepository() {
        return null;
    }

}

