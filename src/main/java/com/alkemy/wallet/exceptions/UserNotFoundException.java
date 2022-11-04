package com.alkemy.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.experimental.StandardException;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
@StandardException
public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

}
