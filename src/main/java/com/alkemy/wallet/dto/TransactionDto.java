package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class TransactionDto {
    private Long id;
    private Double amount;
    private EType type;
    private String description;
    private Account accountId;
    private Timestamp transactionDate;
}

