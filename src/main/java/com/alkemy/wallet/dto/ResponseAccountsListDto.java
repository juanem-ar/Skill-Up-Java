package com.alkemy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAccountsListDto {
    int totalPages;
    String previousPage;
    String nextPage;
    List<ResponseAccountDto> accountsDto;
}
