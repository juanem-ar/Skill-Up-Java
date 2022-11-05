package com.alkemy.wallet.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiSubError {
    private final  String field;
    private final String message;
}
