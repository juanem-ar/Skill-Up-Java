package com.alkemy.wallet.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class RequestSendUSDTransactionDto extends ResponseSendTransactionDto{

    @NotNull
    @Schema(type = "double", example = "5500.50", maxLength = 1000, required = true)
    private Double amount;

    @Nullable
    @Schema(type = "string", example = "Money sent in USD.", required = true)
    private String description;

    @Schema(type = "long", example = "4", required = true)
    private Long receiverAccountId;
}
