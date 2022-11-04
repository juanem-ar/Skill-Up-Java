package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.mapper.ITransactionMapper;
//import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TransactionServiceImpl implements ITransactionService {

    private ITransactionRepository iTransactionRepository;
    private ITransactionMapper iTransactionMapper;

    @Autowired
    public TransactionServiceImpl(ITransactionRepository iTransactionRepository, ITransactionMapper iTransactionMapper) {
        this.iTransactionRepository = iTransactionRepository;
        this.iTransactionMapper = iTransactionMapper;
    }

    @Override
    public TransactionDtoPay payment( TransactionDtoPay transitionDtoPay) {
       /* Transaction transaction = iTransactionMapper.transactionDtoToTransaction(transitionDtoPay);
        transaction.setType(EType.PAYMENT);
        transaction.setAccountId(1L);
        iTransactionRepository.save(transaction);
        //date files handle default value in model
        TransactionDtoPay  transactionDtoPay = iTransactionMapper.transactionToTransactionDto(transaction);
        return transactionDtoPay;*/
        return null;
    };
}
