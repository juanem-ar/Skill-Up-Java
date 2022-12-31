package com.alkemy.wallet.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import com.alkemy.wallet.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alkemy.wallet.service.IAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/accounts")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AccountController {
    private final IAccountService iAccountService;

    @Operation(method = "GET", summary = "listAccountsByUser", description = "Get all accounts from user. Only use by admin.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",content = @Content(mediaType = "application/json", array = @ArraySchema( schema = @Schema(implementation = ResponseAccountDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping("/{id}")
    public ResponseEntity<List<ResponseAccountDto>> listAccountsByUser(@PathVariable Long id){
        return new ResponseEntity<>(iAccountService.findAllByUser(id), HttpStatus.OK);
    }

    @Operation(method = "GET", summary = "findAllAccounts", description = "Get all system accounts. Only use by admin.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseAccountsListDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping
    public ResponseEntity<ResponseAccountsListDto> findAllAccounts(@RequestParam(name = "page") Integer page, HttpServletRequest httpServletRequest) throws Exception {
        return ResponseEntity.ok(iAccountService.findAll(page, httpServletRequest));
    }

    @Operation(method = "PATCH", summary = "updateAccount", description = "Actualizar una cuenta.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseAccountDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_USER" })
    @PatchMapping("{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long id, Authentication authentication, @Valid @RequestParam(name = "limit") Double transactionLimit) throws Exception{
        return new ResponseEntity<>(iAccountService.updateAccount(id,transactionLimit,authentication), HttpStatus.OK);
    }

    @Operation(method = "GET", summary = "getAccountBalance", description = "Get balance list by user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserBalanceDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/balance")
	public ResponseEntity<ResponseUserBalanceDto> getAccountsBalance(Authentication authentication) {
		return ResponseEntity.ok(iAccountService.getBalance(authentication));
	}

    @Operation(method = "POST", summary = "createAccount", description = "Create an account by a specific currency.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "string", schema = @Schema(example = "Created"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping
    public ResponseEntity<String> createAccount(@Valid @RequestParam(name = "currency") String currency, Authentication authentication) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(iAccountService.addAccount(authentication.getName(), currency));
    }
}
