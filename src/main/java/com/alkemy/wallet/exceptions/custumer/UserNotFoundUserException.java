package com.alkemy.wallet.exceptions.custumer;

public class UserNotFoundUserException extends RuntimeException{
    public UserNotFoundUserException(String message){
        super(message);
    }
}
