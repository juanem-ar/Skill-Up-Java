package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResponseAccountDto {

    @JsonProperty("id")
    @Schema(type = "double", example = "1")
    private int id;

    @JsonProperty("balance")
    @Schema(type = "double", example = "100.5")
    private Double balance;

    @JsonProperty("currency")
    @Schema(type = "double", example = "USD")
    private String currency;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("updateDate")
    private String updateDate;

    @JsonProperty("transactionLimit")
    @Schema(type = "double", example = "1000")
    private Double transactionLimit;
}
