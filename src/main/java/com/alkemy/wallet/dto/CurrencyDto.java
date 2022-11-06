package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {
    private ECurrency currency;
}
