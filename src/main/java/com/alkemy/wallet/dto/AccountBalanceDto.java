package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDto {

	@Schema(type = "long", example = "1")
	private Long id;
	@Schema(type = "ECurrency", example = "USD")
	private ECurrency currency;
	@Schema(type = "double", example = "1000")
	private Double balance;
}
