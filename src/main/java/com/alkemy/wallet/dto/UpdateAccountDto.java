package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountDto {
    @JsonProperty("transactionLimit")
    @Schema(type = "double", example = "1000")
    private Double transactionLimit;
}

