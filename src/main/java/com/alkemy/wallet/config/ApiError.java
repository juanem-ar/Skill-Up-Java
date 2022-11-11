package com.alkemy.wallet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
@Setter
@Getter
public class ApiError {
    private HttpStatus status;
    private String mensaje;
    private int cantidadDeErrores;
    private List<ApiSubError> subError;
}
