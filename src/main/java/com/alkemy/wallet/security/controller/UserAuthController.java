package com.alkemy.wallet.security.controller;

import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;
import com.alkemy.wallet.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.*;

@Validated
@RequestMapping("/auth")
@RestController
@Tag(name = "Authentication", description = "Register and Login to use the app")
public class UserAuthController {
    @Autowired
    private IAuthenticationService iAuthenticationService;

    @Operation(method = "POST", summary = "signUp and signIn", description = "Register and Login to app",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Account registered and logged!", content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseUserDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content)
            })
    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> signUp(@Valid @RequestBody RequestUserDto user) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(iAuthenticationService.saveUser(user));
    }

    @Operation(method = "POST", summary = "signIn", description = "Login to the app",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account logged!", content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> signIn(@Valid @RequestBody AuthenticationRequestDto authRequest) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(iAuthenticationService.login(authRequest));
    }

}




