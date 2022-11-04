package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.model.Transaction;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ITransactionMapper   {
    TransactionDtoPay  transactionToTransactionDto (Transaction transaction);
    Transaction transactionDtoToTransaction  (TransactionDtoPay transactionDtoPay);

}
