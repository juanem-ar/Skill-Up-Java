package com.alkemy.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Setter
@Getter
public class TransactionDtoPay {

    @DecimalMin(value = "0.01",message = "Amount must be greater than 0")
    @Schema(type = "double", example = "980.75")
    private Double amount;

    @Schema(type = "string", example = "Tax payment")
    private String description;

    @Schema(type = "long", example = "1")
    private Long senderAccountId;

    @Schema(type = "long", example = "1")
    private Long receiverAccountId;
}
