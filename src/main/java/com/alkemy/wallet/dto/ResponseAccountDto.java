package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAccountDto {

    @JsonProperty("id")
    @Schema(type = "long", example = "1")
    private Long id;

    @JsonProperty("balance")
    @Schema(type = "double", example = "100.5")
    private Double balance;

    @JsonProperty("currency")
    @Schema(type = "String", example = "USD")
    private String currency;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("updateDate")
    private String updateDate;

    @JsonProperty("transactionLimit")
    @Schema(type = "double", example = "1000.0")
    private Double transactionLimit;
}
