package com.alkemy.wallet.exceptions.messageCostumerErros;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
public class ErrorsResponseMessage {
   private HttpStatus statusCode;
   private String message;
}
