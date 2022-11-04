package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account.Currency;

import lombok.Data;

@Data
public class AccountBalanceDto {
	private Long id;
	private Currency currency;
	private Double balance;
}
