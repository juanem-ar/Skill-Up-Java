package com.alkemy.wallet.mapper;


import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.dto.ResponseTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;


@Mapper(componentModel = "spring")
public interface ITransactionMapper   {
    /*@Mapping(target = "idAccount", source = "account.id")
    TransactionDtoPay  transactionToTransactionDto (Transaction transaction);

    @Mapping( target = "account.id",source = "idAccount")
    Transaction transactionDtoToTransaction  (TransactionDtoPay transactionDtoPay);*/

    ResponseTransactionDto modelToResponseTransactionDto(Transaction transaction);
    Transaction responseTransactionDtoToModel(ResponseTransactionDto dto);
    List<ResponseTransactionDto> listModelToResponseTransactionDto(List<Transaction> ListTransaction);
    List<Transaction> ListResponseTransactionDtoToModel(List<ResponseTransactionDto> dto);

}



