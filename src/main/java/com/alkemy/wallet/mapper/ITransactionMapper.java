package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ITransactionMapper   {
    @Mapping(target = "idAccount", source = "account.id")
    TransactionDtoPay  transactionToTransactionDto (Transaction transaction);
    @Mapping( target = "account.id",source = "idAccount")
    Transaction transactionDtoToTransaction  (TransactionDtoPay transactionDtoPay);

}
