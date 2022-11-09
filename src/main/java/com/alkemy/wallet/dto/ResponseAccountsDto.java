package com.alkemy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAccountsDto {
    List<ResponseAccountDto> accountsDto;
    String previousPage;
    String nextpage;

}
