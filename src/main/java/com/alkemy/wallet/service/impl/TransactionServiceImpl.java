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

import java.util.List;

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

    // Faltan findArsAccountByUserId
    /*
    public ResponseTransactionDto sendArs(String token, Long accountId, Long amount, EType type) {

        Long senderId = jwtUtils.extractUserId(getJwt(token));
        ResponseTransactionDto transaction = null;
        //Account senderAccount = accountRepository.findArsAccountByUserId(senderId);
        Long receiverId = accountRepository.findArsAccountByUserId(accountId);

        if (amount <= senderAccount.getBalance() && amount <= senderAccount.getTransactionLimit()) {

            transaction = transactionMapper.modelToResponseTransactionDto(payment(senderId, receiverId, amount, type));
            income(accountId, receiverId, amount, EType.INCOME);
            log.info("Successful ARS transaction");

            save(transaction);
        } else {
            log.error("ARS transaction failed");
        }
        return transaction;
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

	@Override
	public List<Transaction> findAllTransactionsWith(
		Account account) {
		return transactionRepository.findByAccount(account);
	}
}

    


