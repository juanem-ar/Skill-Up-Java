package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ITransactionMapper {

    ResponseTransactionDto modelToResponseTransactionDto(Transaction transaction);

    Transaction responseTransactionDtoToModel(ResponseTransactionDto dto);
}
