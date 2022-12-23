package com.alkemy.wallet.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionPageDto {
    String nextPage;
    String previousPage;
    int totalPages;
    List<ResponseTransactionDto> transactionDtoList;
}
