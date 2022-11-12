package com.alkemy.wallet.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class ResponseFixedDepositDto {
  private Double amount;
  private Double interest;
  private Timestamp creationDate;
  private Timestamp closingDate;
}
