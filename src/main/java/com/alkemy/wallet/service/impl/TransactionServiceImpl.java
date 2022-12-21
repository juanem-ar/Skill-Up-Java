package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.mapper.ITransactionMapper;
import com.alkemy.wallet.model.*;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.security.service.IJwtUtils;
import com.alkemy.wallet.service.ITransactionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.exceptions.ErrorEnum;
import com.alkemy.wallet.exceptions.TransactionError;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements ITransactionService {
    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;
    private final ITransactionMapper transactionMapper;
    private final IJwtUtils jwtUtils;

    public ResponseTransactionDto send(Long senderId, ResponseSendTransactionDto responseSendTransactionDto, ECurrency currency) throws TransactionError {
        Account senderAccount = accountRepository.getReferenceByUserIdAndCurrency(senderId, currency);
        Account receiverAccount = accountRepository.getReferenceById(responseSendTransactionDto.getReceiverAccountId());
        String description = "Money transfer in " + currency;

        TransactionDtoPay sendTransaction = new TransactionDtoPay();
        sendTransaction.setAmount(responseSendTransactionDto.getAmount());
        sendTransaction.setType(EType.PAYMENT);
        sendTransaction.setDescription(description);
        sendTransaction.setAccountId(senderAccount.getId());

        TransactionDtoPay receiveTransaction = new TransactionDtoPay();
        receiveTransaction.setAmount(responseSendTransactionDto.getAmount());
        receiveTransaction.setType(EType.INCOME);
        receiveTransaction.setDescription(description);
        receiveTransaction.setAccountId(responseSendTransactionDto.getReceiverAccountId());

        ResponseTransactionDto transaction = new ResponseTransactionDto();

        if(receiverAccount != null) {
            if (senderId != receiverAccount.getUser().getId()) {
                if (senderAccount.getId() != receiverAccount.getId()) {
                    if (receiverAccount.getCurrency().equals(currency)) {
                        if (sendTransaction.getAmount() <= senderAccount.getBalance()) {
                            if (sendTransaction.getAmount() <= senderAccount.getTransactionLimit()) {

                                transaction = payment(sendTransaction);
                                payment(receiveTransaction);

                            } else {
                                throw new TransactionError("Amount trying to send is above the transaction limit");
                            }
                        } else {
                            throw new TransactionError("Insufficient balance");
                        }
                    } else {
                        throw new TransactionError("Trying to send money to an account that holds another currency");
                    }
                } else {
                    throw new TransactionError("Account trying to receive the money is the same as the one who's sending it");
                }
            } else {
                throw new TransactionError("User trying to receive the money is the same as the one who's sending it");
            }
        } else {
            throw new TransactionError("Receiver account doesn't exist");
        }

        return transaction;
    }

    @Override
    public ResponseTransactionDto payment(TransactionDtoPay transactionDtoPay) {
        Account account = accountRepository.getReferenceById(transactionDtoPay.getAccountId());
        Transaction entity = transactionMapper.transactionDtoToTransaction(transactionDtoPay);
        entity.setAccount(account);
        Double currentBalance = account.getBalance();
        if(entity.getType().equals(EType.PAYMENT)) {
            account.setBalance(currentBalance - entity.getAmount());
        }
        else if(entity.getType().equals(EType.INCOME)) {
            account.setBalance(currentBalance + entity.getAmount());
        }
        transactionRepository.save(entity);
        accountRepository.save(account);
        ResponseTransactionDto response = transactionMapper.modelToResponseTransactionDto(entity);
        return response;
    }

    public ResponseTransactionDto save(RequestTransactionDto transactionDto){
        if (transactionDto.getAmount() <= 0) {
            throw new TransactionError(ErrorEnum.DEPOSITNOTVALID.getMessage());
        }
        Account account = accountRepository.getReferenceById(transactionDto.getAccountId());
        transactionDto.setType(EType.DEPOSIT);
        account.setBalance(account.getBalance() + transactionDto.getAmount());
        Transaction entity = transactionMapper.requestTransactionDtoToModel(transactionDto);
        entity.setAccount(account);
        Transaction entitySaved = transactionRepository.save(entity);
        return transactionMapper.modelToResponseTransactionDto(entitySaved);
    }

	@Override
	public List<Transaction> findAllTransactionsWith(Long accountId) {
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
    public ResponseTransactionDto findResponseTransactionById(Long id, String token) throws Exception {
        return transactionMapper.modelToResponseTransactionDto(findTransactionById(id, token));
    }

    @Override
    public Transaction findTransactionById(Long id, String token) throws Exception {
        if (!transactionRepository.existsById(id))
            throw new TransactionError("This transaction does not exist.");
        Transaction entity = transactionRepository.getReferenceById(id);
        if (entity.getAccount().getUser().getId() != jwtUtils.extractUserId(token))
            throw new TransactionError("This transaction does not belong you");
        return entity;
    }

    @Override
    public ResponseTransactionDto updateDescriptionFromTransaction(Long id, String token, String description) throws Exception {
        Transaction entity = findTransactionById(id,token);
        entity.setDescription(description);
        Transaction entitySaved = transactionRepository.save(entity);
        return transactionMapper.modelToResponseTransactionDto(entitySaved);
    }

}

    



