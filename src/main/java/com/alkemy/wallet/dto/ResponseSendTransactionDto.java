package com.alkemy.wallet.dto;


import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class ResponseSendTransactionDto {

    private Double amount;

    private String description;

    private Long receiverAccountId;

}
