package com.alkemy.wallet.service.impl;



import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.mapper.ITransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.IJwtUtils;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.exceptions.ErrorEnum;
import com.alkemy.wallet.exceptions.TransactionError;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;
    private final ITransactionRepository iTransactionRepository;
    private final ITransactionMapper transactionMapper;
    private final IAccountService accountService;

    @Autowired
    private IJwtUtils jwtUtils;

    public String getJwt(String token){
        String jwt = token.substring(7);
        return jwt;
    }


    public TransactionDtoPay sendArs(Long senderId, Long accountId, Double amount) {

        String description = "Money transfer in ARS";

        Account senderAccount = accountService.getAccountByUserIdAndCurrency(senderId, "ARS");
        long senderAccId = senderAccount.getId();

        Account receiverAccount = accountRepository.getReferenceByUserId(accountId);

        Transaction transaction = new Transaction(amount,description,receiverAccount);
        TransactionDtoPay transactionDto = transactionMapper.transactionToTransactionDto(transaction);
        TransactionDtoPay arsTransaction = null;

        if (amount <= senderAccount.getBalance() && amount <= senderAccount.getTransactionLimit()) {
            arsTransaction = payment(transactionDto);
            //income(accountId, receiverId, amount, EType.INCOME);

        } else {
            log.error("No balance or the amount to send is less than the transaction limit");
        }
        return arsTransaction;
    }

    public TransactionDtoPay sendUsd(Long senderId, Long accountId, Double amount) {

        String description = "Money transfer in USD";

        Account senderAccount = accountService.getAccountByUserIdAndCurrency(senderId, "USD");
        long senderAccId = senderAccount.getId();

        Account receiverAccount = accountRepository.getReferenceByUserId(accountId);

        Transaction transaction = new Transaction(amount,description,receiverAccount);
        TransactionDtoPay transactionDto = transactionMapper.transactionToTransactionDto(transaction);
        TransactionDtoPay usdTransaction = null;

        if (amount <= senderAccount.getBalance() && amount <= senderAccount.getTransactionLimit()) {
            usdTransaction = payment(transactionDto);
            //income(accountId, receiverId, amount, EType.INCOME);

        } else {
            log.error("No balance or the amount to send is less than the transaction limit");
        }
        return usdTransaction;
    }

    @Override
    public TransactionDtoPay payment( TransactionDtoPay transitionDtoPay) {
        Transaction transaction = transactionMapper.transactionDtoToTransaction(transitionDtoPay);
        transaction.setType(EType.PAYMENT);
        iTransactionRepository.save(transaction);
        TransactionDtoPay  transactionDtoPay = transactionMapper.transactionToTransactionDto(transaction);
        return transactionDtoPay;

    }
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
		Long accountId) {
		return transactionRepository.findAllByAccountId(accountId);
	}

    @Override
    public Page<Transaction> findByUserId(Long userId, String token, Pageable pageable) throws Exception{
        if (jwtUtils.extractUserId(token).equals(userId)) {
            return transactionRepository.findByAccount_UserId(userId, pageable);
        } else {
             throw new TransactionError("Token id does not match whit path id");
        }
    }
    @Override
    public Optional<ResponseTransactionDto> findTransactionById(Long id) {
        if (iTransactionRepository.findById(id).isPresent()){
            return Optional.of(transactionMapper.modelToResponseTransactionDto(iTransactionRepository.findById(id).get()));
        }else {
            return Optional.empty();
        }
    }
    @Override
    public ResponseTransactionDto updateDescriptionFromTransaction(ResponseTransactionDto responseTransactionDto, String description) {
        responseTransactionDto.setDescription(description);
        Transaction saveTransaction = transactionMapper.responseTransactionDtoToModel(responseTransactionDto);
        iTransactionRepository.save(saveTransaction);
        return responseTransactionDto;
    }
}

    



