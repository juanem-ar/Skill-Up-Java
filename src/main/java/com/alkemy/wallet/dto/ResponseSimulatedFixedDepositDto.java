package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
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
