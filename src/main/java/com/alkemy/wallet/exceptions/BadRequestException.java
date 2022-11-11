package com.alkemy.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.experimental.StandardException;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@StandardException
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public BadRequestException(String message) {
		super(message);
	}
}