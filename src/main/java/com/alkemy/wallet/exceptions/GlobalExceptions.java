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
    public ResponseEntity<ErrorsResponseMessage> processErrorNotFound(ResourceNotFoundException ex) {
        ErrorsResponseMessage errorsResponseMessage = new ErrorsResponseMessage();
        errorsResponseMessage.setStatusCode(HttpStatus.NOT_FOUND);
        errorsResponseMessage.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorsResponseMessage);
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorsResponseMessage> processErrorBadRequest(BadRequestException ex) {
        ErrorsResponseMessage errorsResponseMessage = new ErrorsResponseMessage();
        errorsResponseMessage.setStatusCode(HttpStatus.BAD_REQUEST);
        errorsResponseMessage.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsResponseMessage);
    }
    @ExceptionHandler({TransactionError.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorsResponseMessage> processErrorTransactionError(TransactionError ex) {
        ErrorsResponseMessage errorsResponseMessage = new ErrorsResponseMessage();
        errorsResponseMessage.setStatusCode(HttpStatus.NOT_FOUND);
        errorsResponseMessage.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorsResponseMessage);
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
