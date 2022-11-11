package com.alkemy.wallet.security.controller;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;
import com.alkemy.wallet.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.*;

@Validated
@RequestMapping("/auth")
@RestController
public class UserAuthController {
    @Autowired
    public IAuthenticationService  authenticationServiceService;

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> signUp(@Valid @RequestBody ResponseUserDto user) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationServiceService.saveUser(user));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> signIn(@Valid @RequestBody AuthenticationRequestDto authRequest) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationServiceService.login(authRequest));
    }

}




