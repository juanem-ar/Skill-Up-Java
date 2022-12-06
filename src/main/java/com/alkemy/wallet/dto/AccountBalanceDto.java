package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDto {

	@Schema(type = "long", example = "1")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@Schema(type = "ECurrency", example = "USD")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ECurrency currency;

	@Schema(type = "double", example = "1000")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Double balance;
}
