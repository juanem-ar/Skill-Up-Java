package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;
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
    private Double amount;
    @NotNull(message = "Invalid currency.")
    private ECurrency currency;
    @NotNull(message = "Invalid period.")
    private Integer period;
    private Timestamp creationDate;
    private Timestamp closingDate;
}
