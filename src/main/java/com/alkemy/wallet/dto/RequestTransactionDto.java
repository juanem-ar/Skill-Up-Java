package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.EType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class RequestTransactionDto {
    @NotNull
    @Schema(type = "double", example = "5500.50", required = true)
    private Double amount;

    @Schema(type = "EType", example = "DEPOSIT")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EType type;

    @Nullable
    @Schema(type = "string", example = "depósito", required = true)
    private String description;

    @Schema(type = "boolean", defaultValue = "false")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean softDelete = Boolean.FALSE;

    @Schema(type = "long", example = "1", required = true)
    private Long accountId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    private Timestamp transactionDate;

}
