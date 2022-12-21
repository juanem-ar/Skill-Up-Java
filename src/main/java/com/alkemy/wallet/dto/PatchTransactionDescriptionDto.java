package com.alkemy.wallet.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PatchTransactionDescriptionDto {

    @NotNull
    @NotEmpty
    private String description;
}
