package com.alkemy.wallet.exceptions;

public enum ErrorEnum {
    DEPOSITNOTVALID;


    public String getMessage() {
        if (ErrorEnum.this == DEPOSITNOTVALID){
            return ("Deposit value must be greater than 0");
        }
        return ("Error undefined");
    }
}
