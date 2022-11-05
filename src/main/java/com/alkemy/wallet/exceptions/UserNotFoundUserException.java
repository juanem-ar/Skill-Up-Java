package com.alkemy.wallet.exceptions;

public class UserNotFoundUserException extends RuntimeException{
    public UserNotFoundUserException(String message){
        super(message);
    }
}
