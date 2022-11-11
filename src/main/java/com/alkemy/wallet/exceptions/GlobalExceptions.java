package com.alkemy.wallet.exceptions;

import com.alkemy.wallet.exceptions.messageCostumerErros.ErrorsResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptions {
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> processErrorNotFound(ResourceNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> processErrorBadRequest(BadRequestException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler({UserNotFoundUserException.class})
    public ResponseEntity<ErrorsResponseMessage> processErrorBadRequest(UserNotFoundUserException ex) {
        ErrorsResponseMessage errorsResponseMessage = new ErrorsResponseMessage();
        errorsResponseMessage.setStatusCode(HttpStatus.BAD_REQUEST);
        errorsResponseMessage.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsResponseMessage);
    }
}
