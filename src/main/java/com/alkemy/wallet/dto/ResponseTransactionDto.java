package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.EType;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

@Getter
@Setter
public class ResponseTransactionDto {

    @Schema(type = "long", example = "1")
    private Long id;

    @NotNull
    @Schema(type = "double", example = "5500.50")
    private Double amount;

    @Schema(type = "EType", example = "ARS")
    private EType type;

    @Nullable
    @Schema(type = "string", example = "depósito")
    private String description;

    @Schema(type = "boolean", example = "false")
    private Boolean softDelete = Boolean.FALSE;

    @Schema(type = "long", example = "1")
    private Long accountId;
    private Account account;
    private Timestamp transactionDate;


}
