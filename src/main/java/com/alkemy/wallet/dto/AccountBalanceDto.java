package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;

import lombok.Data;

@Data
public class AccountBalanceDto {
	private Long id;
	private ECurrency currency;
	private Double balance;
}
