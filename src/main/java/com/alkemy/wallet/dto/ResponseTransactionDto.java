package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

@Getter
@Setter
public class ResponseTransactionDto {
    private Long id;
    @NotNull
    private Double amount;
    private EType type;
    @Nullable
    private String description;
    private Boolean softDelete = Boolean.FALSE;
    private Long accountId;
    private Account account;
    private Timestamp transactionDate;


}
