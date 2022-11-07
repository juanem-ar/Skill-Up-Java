package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountDto {
    @JsonProperty("transactionLimit")
    private Double transactionLimit;
}

