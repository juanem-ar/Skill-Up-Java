package com.alkemy.wallet.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ResponseUserBalanceDto {
	private Long id;

	private List<AccountBalanceDto> accountBalanceDtos =
		new ArrayList<>();

}
