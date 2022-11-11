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
    private final ITransactionMapper transactionMapper;
    private final IAccountService accountService;

    @Autowired
    private IJwtUtils jwtUtils;


    public TransactionDtoPay sendArs(Long senderId, Long accountId, Double amount) {
        String description = "Money transfer in ARS";
        Account senderAccount = accountService.getAccountByUserIdAndCurrency(senderId, "ARS");
        Account receiverAccount = accountService.getAccountByUserIdAndCurrency(accountId, "ARS");

        Transaction sendTransaction = new Transaction();
        sendTransaction.setAmount(amount);
        sendTransaction.setDescription(description);
        sendTransaction.setAccount(senderAccount);

        Transaction receiveTransaction = new Transaction();
        receiveTransaction.setAmount(amount);
        receiveTransaction.setDescription(description);
        receiveTransaction.setAccount(receiverAccount);

        ResponseTransactionDto sendTransactionDto = transactionMapper.modelToResponseTransactionDto(sendTransaction);
        ResponseTransactionDto receiveTransactionDto = transactionMapper.modelToResponseTransactionDto(receiveTransaction);
        TransactionDtoPay arsTransaction = new TransactionDtoPay();

        if (amount <= senderAccount.getBalance() && amount <= senderAccount.getTransactionLimit()) {
            ResponseTransactionDto senderPayment = payment(sendTransactionDto, EType.PAYMENT);
            ResponseTransactionDto receiverIncome = payment(receiveTransactionDto, EType.INCOME);

            if (senderPayment != null && receiverIncome != null){
                arsTransaction.setAmount(amount);
                arsTransaction.setDescription(description);
                arsTransaction.setSenderAccountId(senderAccount.getId());
                arsTransaction.setReceiverAccountId(receiverAccount.getId());

                //transactionRepository.save(transactionMapper.transactionDtoToTransaction(arsTransaction));
            }
        } else {
            log.error("No balance or the amount to send is less than the transaction limit");
        }
        return arsTransaction;
    }

    public TransactionDtoPay sendUsd(Long senderId, Long accountId, Double amount) {
        String description = "Money transfer in USD";
        Account senderAccount = accountService.getAccountByUserIdAndCurrency(senderId, "USD");
        Account receiverAccount = accountService.getAccountByUserIdAndCurrency(accountId, "USD");

        Transaction sendTransaction = new Transaction();
        sendTransaction.setAmount(amount);
        sendTransaction.setDescription(description);
        sendTransaction.setAccount(senderAccount);

        Transaction receiveTransaction = new Transaction();
        receiveTransaction.setAmount(amount);
        receiveTransaction.setDescription(description);
        receiveTransaction.setAccount(receiverAccount);

        ResponseTransactionDto sendTransactionDto = transactionMapper.modelToResponseTransactionDto(sendTransaction);
        ResponseTransactionDto receiveTransactionDto = transactionMapper.modelToResponseTransactionDto(receiveTransaction);
        TransactionDtoPay usdTransaction = new TransactionDtoPay();

        if (amount <= senderAccount.getBalance() && amount <= senderAccount.getTransactionLimit()) {
            ResponseTransactionDto senderPayment = payment(sendTransactionDto, EType.PAYMENT);
            ResponseTransactionDto receiverIncome = payment(receiveTransactionDto, EType.INCOME);

            if (senderPayment != null && receiverIncome != null){
                usdTransaction.setAmount(amount);
                usdTransaction.setDescription(description);
                usdTransaction.setSenderAccountId(senderAccount.getId());
                usdTransaction.setReceiverAccountId(receiverAccount.getId());

                //transactionRepository.save(transactionMapper.transactionDtoToTransaction(usdTransaction));
            }
        } else {
            log.error("No balance or the amount to send is less than the transaction limit");
        }
        return usdTransaction;
    }

    @Override
    public ResponseTransactionDto payment(ResponseTransactionDto transactionDto, EType type) {
        Transaction transaction = transactionMapper.responseTransactionDtoToModel(transactionDto);
        transaction.setType(type);

        Account account = transaction.getAccount();

        if(type.equals(EType.PAYMENT))
        {
            Double currentBalance = account.getBalance();
            account.setBalance(currentBalance - transaction.getAmount());
        }
        else if(type.equals(EType.INCOME))
        {
            Double currentBalance = account.getBalance();
            account.setBalance(currentBalance + transaction.getAmount());
        }

        transactionRepository.save(transaction);
        ResponseTransactionDto savedTransaction = transactionMapper.modelToResponseTransactionDto(transaction);

        return savedTransaction;
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
        if (transactionRepository.findById(id).isPresent()){
            return Optional.of(transactionMapper.modelToResponseTransactionDto(transactionRepository.findById(id).get()));
        }else {
            return Optional.empty();
        }
    }
    @Override
    public ResponseTransactionDto updateDescriptionFromTransaction(ResponseTransactionDto responseTransactionDto, String description) {
        responseTransactionDto.setDescription(description);
        Transaction saveTransaction = transactionMapper.responseTransactionDtoToModel(responseTransactionDto);
        transactionRepository.save(saveTransaction);
        return responseTransactionDto;
    }
}

    



