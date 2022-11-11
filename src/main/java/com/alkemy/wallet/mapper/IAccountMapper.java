package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.AccountBalanceDto;
import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.model.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IAccountMapper {

    List<ResponseAccountDto> accountsToAccountsDto(List<Account> account);

    AccountBalanceDto accountToBalanceDto(Account account);

    ResponseAccountDto accountToAccountDto(Account account);

    Account accountDtoToAccount(ResponseAccountDto responseAccountDto);
}
