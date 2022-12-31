package com.alkemy.wallet.exceptions;

public enum ErrorEnum {
    DEPOSIT_NOT_VALID;

    public String getMessage() {
        if (ErrorEnum.this == DEPOSIT_NOT_VALID){
            return ("Deposit value must be greater than 0");
        }
        return ("Error undefined");
    }
}
