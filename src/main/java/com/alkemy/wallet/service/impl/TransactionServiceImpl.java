package com.alkemy.wallet.service.impl;

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
}
