package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.AccountBalanceDto;
import com.alkemy.wallet.dto.RequestTransactionDto;
import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.dto.ResponseTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = AccountBalanceDto.class)
public interface ITransactionMapper   {
    @Mapping( target = "account.id",source = "accountId")
    Transaction transactionDtoToTransaction  (TransactionDtoPay transactionDtoPay);
    @Mapping(target = "account", source = "account")
    ResponseTransactionDto modelToResponseTransactionDto(Transaction transaction);
    @Mapping(target = "account", source = "account")
    Transaction responseTransactionDtoToModel(ResponseTransactionDto dto);
    @Mapping( target = "account.id",source = "accountId")
    Transaction requestTransactionDtoToModel(RequestTransactionDto dto);
    List<ResponseTransactionDto> listModelToResponseTransactionDto(List<Transaction> list);
    List<Transaction> ListResponseTransactionDtoToModel(List<ResponseTransactionDto> dto);

}



