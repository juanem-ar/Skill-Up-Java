package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedDepositDto {

    @NotNull(message = "Invalid amount.")
    @Schema(required = true, example = "1000", description = "Fixed deposit", minLength = 1)
    private Double amount;

    @NotNull(message = "Invalid currency.")
    @Schema(required = true, example = "ARS", description = "Currency ARS or USD")
    private ECurrency currency;

    @NotNull(message = "Invalid period.")
    @Schema(required = true, example = "30", description = "Fixed deposit period", minLength = 30)
    private Integer period;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp closingDate;
}
