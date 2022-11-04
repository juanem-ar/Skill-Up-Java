package com.alkemy.wallet.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Setter
@Getter
public class TransactionDtoPay {

    @DecimalMin(value = "0.01",message = "the amount bigger to number 0")
    private Double amount;

    private String description;
}
