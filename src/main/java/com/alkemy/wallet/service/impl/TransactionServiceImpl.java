package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.generic.GenericServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.exceptions.ErrorEnum;
import com.alkemy.wallet.exceptions.TransactionError;
import com.alkemy.wallet.mapper.ITransactionMapper;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl extends GenericServiceImpl<Transaction, Long> implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;
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

    // Si queda el extends GenericService, el método del conflicto sería asi
    public ResponseTransactionDto save(ResponseTransactionDto transactionDto){
        if (transactionDto.getAmount() <= 0) {
            throw new TransactionError(ErrorEnum.DEPOSITNOTVALID.getMessage());
        }
        transactionDto.setType(EType.DEPOSIT);
        Transaction entity = transactionMapper.toEntity(transactionDto); // Pero toEntity recibe TransactionDto, y transactionDto es un ResponseTransactionDto
        Transaction entitySaved = transactionRepository.save(entity);
        return transactionMapper.toDto(entitySaved);
    }

    @Override
    public Transaction save(Transaction entity) {
        return transactionRepository.save(entity);
    }

    @Override
    public JpaRepository<Transaction, Long> getRepository() {
        return null;
    }

}
/*
// Conflicto

@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private ITransactionMapper transactionMapper;

    @Autowired
    private ITransactionRepository transactionRepository;

    @Override
    public ResponseTransactionDto save(ResponseTransactionDto transactionDto){
        if (transactionDto.getAmount() <= 0) {
            throw new TransactionError(ErrorEnum.DEPOSITNOTVALID.getMessage());
        }
        transactionDto.setType(EType.DEPOSIT);
        Transaction entity = transactionMapper.responseTransactionDtoToModel(transactionDto);
        Transaction entitySaved = transactionRepository.save(entity);
        return transactionMapper.modelToResponseTransactionDto(entitySaved);
    }
}*/
