package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.mapper.ITransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.exceptions.ErrorEnum;
import com.alkemy.wallet.exceptions.TransactionError;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;
    private final ITransactionMapper transactionMapper;

    @Autowired
    private JwtUtils jwtUtils;

    public String getJwt(String token){
        String jwt = token.substring(7);
        return jwt;
    }

    // Comentadas porque faltan funcionalidades llamadas dentro (de Account e income)
    /*
    public ResponseTransactionDto sendArs(String token, Long accountId, Double amount) {

        Long senderId = jwtUtils.extractUserId(getJwt(token));

        String description = "Money transfer in ARS";
        //Account senderAccount = accountRepository.findUsdAccountByUserId(senderId);
        //Long receiverAccount = accountRepository.findArsAccountByUserId(accountId);
        TransactionDtoPay transactionDtoPay = new TransactionDtoPay(amount, description, receiverId);
        TransactionDtoPay arsTransaction = null;

        if (amount <= senderAccount.getBalance() && amount <= senderAccount.getTransactionLimit()) {

            arsTransaction = payment(transactionDtoPay);
            //income(accountId, receiverId, amount, EType.INCOME);
            log.info("Successful ARS transaction");
            save(arsTransaction);

        } else {
            log.error("ARS transaction failed");
        }
        return arsTransaction;
    }

    public ResponseTransactionDto sendUsd(String token, Long accountId, Double amount) {

        Long senderId = jwtUtils.extractUserId(getJwt(token));

        String description = "Money transfer in USD";
        // Account senderAccount = accountRepository.findUsdAccountByUserId(senderId);
        //Long receiverAccount = accountRepository.findUsdAccountByUserId(accountId);

        TransactionDtoPay transactionDtoPay = new TransactionDtoPay(amount, description, receiverId);
        TransactionDtoPay usdTransaction = null;

        if (amount <= senderAccount.getBalance() && amount <= senderAccount.getTransactionLimit()) {

            usdTransaction = payment(transactionDtoPay);
            //income(accountId, receiverId, amount, EType.INCOME);
            log.info("Successful USD transaction");
            save(usdTransaction);

        } else {
            log.error("USD transaction failed");
        }
        return usdTransaction;
    }*/

    public ResponseTransactionDto save(ResponseTransactionDto transactionDto){
        if (transactionDto.getAmount() <= 0) {
            throw new TransactionError(ErrorEnum.DEPOSITNOTVALID.getMessage());
        }
        transactionDto.setType(EType.DEPOSIT);
        Transaction entity = transactionMapper.responseTransactionDtoToModel(transactionDto);
        Transaction entitySaved = transactionRepository.save(entity);
        return transactionMapper.modelToResponseTransactionDto(entitySaved);
    }
}

    


