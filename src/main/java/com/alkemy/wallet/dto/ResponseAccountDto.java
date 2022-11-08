package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAccountDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("balance")
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
