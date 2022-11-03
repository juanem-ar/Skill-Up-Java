package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.generic.GenericServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl extends GenericServiceImpl<Transaction, Long> implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final IUserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    /*
    public TransactionDto sendArs(Long senderId, Long accountId, Long amount, EType type) {

        Account senderAccount = accountRepository.findArsAccountByUserId(userId);
        Long receiverId = userRepository.findAccountByUserId(accountId);

        if (amount <= senderAccount.getBalance() && amount <= senderAccount.getTransactionLimit()) {
            TransactionDto transaction = transactionMapper.toDto(payment(senderId, receiverId, amount, type));
            income(accountId, receiverId, amount, EType.INCOME);
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

