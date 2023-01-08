package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.mapper.ITransactionMapper;
import com.alkemy.wallet.model.*;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.exceptions.ErrorEnum;
import com.alkemy.wallet.exceptions.TransactionError;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements ITransactionService {
    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;
    private final IUserRepository iUserRepository;
    private final ITransactionMapper transactionMapper;
    public static final Integer TRANSACTIONS_FOR_PAGE = 10;

    public ResponseTransactionDto send(Authentication authentication, Double amount, String description, Long accountId,ECurrency currency) throws Exception {

        Long senderId = iUserRepository.findByEmail(authentication.getName()).getId();

        Account senderAccount = accountRepository.getReferenceByUserIdAndCurrency(senderId, currency);

        Account receiverAccount = accountRepository.getReferenceByIdAndCurrency(accountId, currency);
        if (receiverAccount == null || senderAccount.getId() == receiverAccount.getId())
            throw new TransactionError("The selected account does not exist or belongs to you or has another currency");

        TransactionDtoPay sendTransaction = new TransactionDtoPay();
        sendTransaction.setAmount(amount);
        sendTransaction.setType(EType.PAYMENT);
        sendTransaction.setDescription(description + ". Money send in " + currency);
        sendTransaction.setAccountId(senderAccount.getId());

        TransactionDtoPay receiveTransaction = new TransactionDtoPay();
        receiveTransaction.setAmount(amount);
        receiveTransaction.setType(EType.INCOME);
        receiveTransaction.setDescription(description + ". Money send in " + currency);
        receiveTransaction.setAccountId(accountId);

        if (sendTransaction.getAmount() > senderAccount.getTransactionLimit())
            throw new TransactionError("Amount trying to send is above the transaction limit");

        ResponseTransactionDto transaction = payment(sendTransaction);
        payment(receiveTransaction);

        return transaction;
    }

    @Override
    public ResponseTransactionDto payment(TransactionDtoPay transactionDtoPay) throws Exception {

        Account account = accountRepository.getReferenceById(transactionDtoPay.getAccountId());

        Transaction entity = transactionMapper.transactionDtoToTransaction(transactionDtoPay);

        entity.setAccount(account);

        Double currentBalance = account.getBalance();
        if (transactionDtoPay.getAmount() <= 0)
            throw new TransactionError(ErrorEnum.DEPOSIT_NOT_VALID.getMessage());

        if(entity.getType().equals(EType.PAYMENT)) {
            if(currentBalance < transactionDtoPay.getAmount())
                throw new TransactionError("insufficient balance");
            account.setBalance(currentBalance - entity.getAmount());
        } else if(entity.getType().equals(EType.INCOME)) {
            account.setBalance(currentBalance + entity.getAmount());
        }
        Transaction entitySaved = transactionRepository.save(entity);
        Account accountSaved = accountRepository.save(account);

        return transactionMapper.modelToResponseTransactionDto(entity);
    }

    public ResponseTransactionDto save(Double amount, String description, Long accountId, Authentication authentication) throws Exception {
        RequestTransactionDto transactionDto = new RequestTransactionDto();
        transactionDto.setAmount(amount);
        transactionDto.setDescription(description);
        transactionDto.setAccountId(accountId);

        Long userId = iUserRepository.findByEmail(authentication.getName()).getId();

        if (transactionDto.getAmount() <= 0) {
            throw new TransactionError(ErrorEnum.DEPOSIT_NOT_VALID.getMessage());
        }

        Account account = accountRepository.findByIdAndUserId(transactionDto.getAccountId(), userId);
        if (account == null){
            throw new TransactionError("Insert your account id");
        }
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
    public List<ResponseTransactionDto> findAllTransactionsByUserId(Authentication authentication) throws Exception {
        Long userId = iUserRepository.findByEmail(authentication.getName()).getId();
        List<Account> accountList = accountRepository.findAllByUserId(userId);
        List<Transaction> transactionsList = transactionRepository.findAllByAccountIn(accountList);
        if (transactionsList.size()==0)
            throw new TransactionError("You have not made any transaction");
        return transactionMapper.listModelToResponseTransactionDto(transactionsList);
    }

    @Override
    public TransactionPageDto findAllByAccount(Integer page, HttpServletRequest httpServletRequest) throws Exception {
        if (page <= 0)
            throw new TransactionError("You request page not found, try page 1");

        Pageable pageWithTenElementsAndSortedByAccountAscAndAmountDesc = PageRequest.of(page-1,TRANSACTIONS_FOR_PAGE,
                Sort.by("account.id")
                        .ascending()
                        .and(Sort.by("amount")
                                .descending()));
        Page<Transaction> transactionPage = transactionRepository.findAll(pageWithTenElementsAndSortedByAccountAscAndAmountDesc);

        //Pagination DTO
        TransactionPageDto pagination = new TransactionPageDto();
        int totalPages = transactionPage.getTotalPages();
        pagination.setTotalPages(totalPages);

        if (page > totalPages)
            throw new TransactionError("The page you request not found, try page 1 or go to previous page");

        // url
        String url = httpServletRequest
                .getRequestURL().toString() + "?" + "page=";

        pagination.setNextPage(totalPages == page ? null : url + String.valueOf(page + 1));
        pagination.setPreviousPage(page == 1 ? null : url + String.valueOf(page - 1));
        pagination.setTransactionDtoList(transactionMapper.listModelToResponseTransactionDto(transactionPage.getContent()));
        return pagination;
    }

    @Override
    public ResponseTransactionDto findResponseTransactionById(Long id, Authentication authentication) throws Exception {
        return transactionMapper.modelToResponseTransactionDto(findTransactionById(id, authentication));
    }

    @Override
    public Transaction findTransactionById(Long id, Authentication authentication) throws Exception {
        if (!transactionRepository.existsById(id))
            throw new TransactionError("This transaction does not exist.");
        Transaction entity = transactionRepository.getReferenceById(id);
        if (!entity.getAccount().getUser().getEmail().equals(authentication.getName()))
            throw new TransactionError("This transaction does not belong you");
        return entity;
    }

    @Override
    public ResponseTransactionDto updateDescriptionFromTransaction(Long id, Authentication authentication, String description) throws Exception {
        Transaction entity = findTransactionById(id,authentication);
        entity.setDescription(description);
        Transaction entitySaved = transactionRepository.save(entity);
        return transactionMapper.modelToResponseTransactionDto(entitySaved);
    }

}

    



