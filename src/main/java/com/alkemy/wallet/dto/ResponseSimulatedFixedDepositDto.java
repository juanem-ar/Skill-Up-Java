package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ResponseSimulatedFixedDepositDto {

    @NotNull(message = "Invalid amount.")
    private Double amount;
    @NotNull(message = "Invalid currency.")
    private ECurrency currency;
    @NotNull(message = "Invalid period.")
    private Integer period;
    private LocalDate CreationLocalDate;
    private LocalDate ClosingLocalDate;
    private Double totalAmount;
    private Double interest;
}
