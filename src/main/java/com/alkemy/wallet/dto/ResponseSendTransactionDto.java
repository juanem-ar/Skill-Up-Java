package com.alkemy.wallet.dto;


import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class ResponseSendTransactionDto {

    @NotNull
    @Schema(type = "double", example = "5500.50")
    private Double amount;

    @Nullable
    @Schema(type = "string", example = "Money sent in ARS")
    private String description;

    @Schema(type = "long", example = "1")
    private Long receiverAccountId;

}
