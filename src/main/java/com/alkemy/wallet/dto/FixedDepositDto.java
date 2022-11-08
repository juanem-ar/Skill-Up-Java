package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedDepositDto {
    private Double amount;
    private ECurrency currency;
    private int period;
    private Timestamp creationDate;
    private Timestamp closingDate;
}
