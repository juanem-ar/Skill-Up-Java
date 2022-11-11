package com.alkemy.wallet.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResponseUserBalanceDto {

	@Schema(type = "long", example = "1")
	private Long id;

	private List<AccountBalanceDto> accountBalanceDtos =
		new ArrayList<>();

}
