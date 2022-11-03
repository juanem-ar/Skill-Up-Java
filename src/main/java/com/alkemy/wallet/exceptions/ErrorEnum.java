package com.alkemy.wallet.exceptions;

public enum ErrorEnum {
    DEPOSITNOTVALID;


    public String getMessage() {
        if (ErrorEnum.this == DEPOSITNOTVALID){
            return ("El deposito debe ser mayor a 0");
        }
        return ("Error indefinido");
    }
}
