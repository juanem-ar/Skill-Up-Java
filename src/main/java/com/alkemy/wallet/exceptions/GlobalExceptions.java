package com.alkemy.wallet.exceptions;

import com.alkemy.wallet.exceptions.messageCostumerErros.ErrorsResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptions {
    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<String> processErrorNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> processErrorBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler({TransactionError.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<String> processErrorTransactionError(TransactionError ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler({UserNotFoundUserException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorsResponseMessage> processErrorBadRequest(UserNotFoundUserException ex) {
        ErrorsResponseMessage errorsResponseMessage = new ErrorsResponseMessage();
        errorsResponseMessage.setStatusCode(HttpStatus.BAD_REQUEST);
        errorsResponseMessage.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsResponseMessage);
    }
}
