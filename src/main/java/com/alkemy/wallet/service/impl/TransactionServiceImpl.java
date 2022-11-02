package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.generic.GenericServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl extends GenericServiceImpl<Transaction, Long> implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final IUserRepository userRepository;
    //private final IAccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

/*
    public TransactionDto sendArs(Long accountId, Long amount, EType type) {

        Long userId = 1L;
        Long accountUserId = userRepository.findAccountByUserId(userId);
        Long receiverId = userRepository.findAccountByUserId(accountId);

        Optional<User> user = userRepository.findById(userId);
        Account account = accountRepository.findAccountArsByUserId(userId);

        if (account.getBalance() >= amount && account.getTransactionLimit() >= amount) {
            TransactionDto transaction = transactionMapper.toDto(payment(userId, accountUserId, amount, type));
            income(accountId, amount, EType.INCOME, receiverId);
            log.info("Transaccion exitosa");
        } else {
            log.error("Fallo en la transaccion ARS");
        }
        return transaction;
    }*/

    @Override
    public JpaRepository<Transaction, Long> getRepository() {
        return null;
    }

    //private final TransactionMapper transactionMapper;


}

