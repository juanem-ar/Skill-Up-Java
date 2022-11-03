package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountDto {

    public static Map<String,Object> accountToDto(Account account){
        Map<String, Object> dto = new HashMap<>();

        dto.put("id",account.getId());
        dto.put("balance",account.getBalance());
        dto.put("currency",account.getCurrency());
        dto.put("deleted",account.getDeleted());
        dto.put("creationDate",account.getCreationDate());
        dto.put("updateDate",account.getUpdateDate());
        dto.put("transactionLimit",account.getTransactionLimit());
        dto.put("user",account.getUserId());

        return dto;
    }

    public static Map<String,Object> accountsToDto(List<Account> accounts){
        Map<String, Object> dto = new HashMap<>();

        dto.put("accounts", accounts.stream().map(AccountDto::accountToDto).toList());
        return dto;
    }

}
