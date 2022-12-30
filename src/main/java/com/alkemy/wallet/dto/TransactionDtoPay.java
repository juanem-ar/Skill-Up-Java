package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.EType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class TransactionDtoPay {

    @NotNull
    @DecimalMin(value = "0.01",message = "Amount must be greater than 0")
    @Schema(type = "double", example = "980.75", minLength = 0, maxLength = 300000)
    private Double amount;

    @Schema(type = "EType", example = "PAYMENT", required = true)
    private EType type;

    @Schema(type = "string", example = "Tax payment", required = true)
    private String description;

    @Schema(type = "string", example = "2", description = "Account id", required = true)
    private Long accountId;
}
