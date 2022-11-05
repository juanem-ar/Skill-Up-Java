package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.mapper.ITransactionMapper;
//import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository iTransactionRepository;
    private final ITransactionMapper iTransactionMapper;


    @Override
    public TransactionDtoPay payment( TransactionDtoPay transitionDtoPay) {
       Transaction transaction = iTransactionMapper.transactionDtoToTransaction(transitionDtoPay);
        transaction.setType(EType.PAYMENT);
        iTransactionRepository.save(transaction);
        TransactionDtoPay  transactionDtoPay = iTransactionMapper.transactionToTransactionDto(transaction);
        return transactionDtoPay;

    };
}
