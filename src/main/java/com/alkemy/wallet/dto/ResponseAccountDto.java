package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAccountDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("balance")
    @Schema(type = "double", example = "1002.5")
    private Double balance;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("updateDate")
    private String updateDate;

    @JsonProperty("transactionLimit")
    private Double transactionLimit;
}
