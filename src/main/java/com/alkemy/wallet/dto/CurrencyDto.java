package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.ECurrency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {
    @Schema(required = true, example = "ARS", description = "Currency type")
    private ECurrency currency;
}
