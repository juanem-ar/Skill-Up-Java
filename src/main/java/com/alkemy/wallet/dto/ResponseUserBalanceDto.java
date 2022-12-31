package com.alkemy.wallet.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ResponseUserBalanceDto {

	private List<AccountBalanceDto> accountBalance = new ArrayList<>();
	private List<ResponseFixedDepositDto> arsFixedDeposits = new ArrayList<>();
	private List<ResponseFixedDepositDto> usdFixedDeposits = new ArrayList<>();

}
