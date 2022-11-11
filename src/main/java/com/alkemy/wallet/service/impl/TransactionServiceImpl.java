package com.alkemy.wallet.service.impl;



import com.alkemy.wallet.dto.ResponseSendTransactionDto;
import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.mapper.ITransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.IJwtUtils;
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

import java.sql.Timestamp;
import java.util.Date;
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

    @Autowired
    private IJwtUtils jwtUtils;

    public ResponseTransactionDto send(Long senderId, ResponseSendTransactionDto responseSendTransactionDto, ECurrency currency) throws TransactionError{
        Account senderAccount = accountRepository.getReferenceByUserIdAndCurrency(senderId, currency);
        Account receiverAccount = accountRepository.getReferenceByUserIdAndCurrency(responseSendTransactionDto.getReceiverAccountId(), currency);

        String description = "Money transfer in " + currency;

        TransactionDtoPay sendTransaction = new TransactionDtoPay();
        sendTransaction.setAmount(responseSendTransactionDto.getAmount());
        sendTransaction.setType(EType.PAYMENT);
        sendTransaction.setDescription(description);
        sendTransaction.setAccountId(senderId);

        TransactionDtoPay receiveTransaction = new TransactionDtoPay();
        receiveTransaction.setAmount(responseSendTransactionDto.getAmount());
        receiveTransaction.setType(EType.INCOME);
        receiveTransaction.setDescription(description);
        receiveTransaction.setAccountId(receiverAccount.getId());

        ResponseTransactionDto transaction = new ResponseTransactionDto();

        if (sendTransaction.getAmount() <= senderAccount.getBalance() && sendTransaction.getAmount() <= senderAccount.getTransactionLimit()) {
            // TODO: senderPayment and receiverIncome try-catch and throw of TransactionError
            payment(sendTransaction);
            payment(receiveTransaction);
        } else {
            throw new TransactionError("No balance or the amount to send is less than the transaction limit");
        }
        return transaction; // TODO: convert to ResponseEntity (fixedtermdeposit)
    }

    @Override
    public ResponseTransactionDto payment(TransactionDtoPay transactionDtoPay) {
        Account account = accountRepository.getReferenceByUserId(transactionDtoPay.getAccountId());

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(transactionDtoPay.getAmount());
        transaction.setType(transactionDtoPay.getType());
        transaction.setDescription(transactionDtoPay.getDescription());
        Date date = new Date();
        transaction.setTransactionDate(new Timestamp(date.getTime()));

        Double currentBalance = account.getBalance();

        if(transaction.getType() == EType.PAYMENT)
        {
            account.setBalance(currentBalance - transaction.getAmount());
        }
        else if(transaction.getType() == EType.INCOME)
        {
            account.setBalance(currentBalance + transaction.getAmount());
        }

        transactionRepository.save(transaction);
        return transactionMapper.modelToResponseTransactionDto(transaction);
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
    public Optional<ResponseTransactionDto> findTransactionById(Long id, String token) throws Exception {
        List<Transaction> transactions = transactionRepository.findByAccount_UserId(jwtUtils.extractUserId(token));
            return Optional.of(transactionMapper.modelToResponseTransactionDto(transactions.stream()
                    .filter(transaction -> transaction.getId().equals(id)).findFirst().get()));
    }
    @Override
    public ResponseTransactionDto updateDescriptionFromTransaction(Long id, String token, String description) throws Exception {
        Optional<ResponseTransactionDto> responseTransactionDto = findTransactionById(id, token);
            Transaction saveTransaction = transactionMapper.responseTransactionDtoToModel(responseTransactionDto.get());
            saveTransaction.setDescription(description);
            transactionRepository.save(saveTransaction);
            return transactionMapper.modelToResponseTransactionDto(saveTransaction);
    }
}

    



