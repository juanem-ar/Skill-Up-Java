package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/transactions")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class TransactionController {
    private final ITransactionService transactionService;

    @Operation(method = "POST", summary = "saveDeposit", description = "Register deposit.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/deposit")
    public ResponseEntity<ResponseTransactionDto> saveDeposit(@Valid Authentication authentication,
                                                              @RequestParam(name = "amount") Double amount,
                                                              @RequestParam(name = "description") String description,
                                                              @RequestParam(name = "account id") Long accountId)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(amount, description, accountId, authentication));
    }

    @Operation(method = "GET", summary = "getListTransactionByUserLogged", description = "Get all transactions from user logged.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping
    public ResponseEntity<List<ResponseTransactionDto>> getListTransactionByUserLogged(Authentication authentication) throws Exception{
        return ResponseEntity.ok(transactionService.findAllTransactionsByUserId(authentication));
    }

    @Operation(method = "GET", summary = "transactions pagination by admin", description = "Order by page with five elements and sorted by account ascending and amount descending",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionPageDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping("/list")
    public ResponseEntity<TransactionPageDto> getAllTransactionPages(@RequestParam(value = "page", defaultValue = "1") @PathVariable int page, HttpServletRequest httpServletRequest) throws Exception{
        return ResponseEntity.ok(transactionService.findAllByAccount(page, httpServletRequest));
    }

    @Operation(method = "GET", summary = "getTransactionByIdAndUserLogged", description = "Get transaction by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{transactionId}")
    public ResponseEntity<ResponseTransactionDto> getTransactionByIdAndUserLogged(Authentication authentication, @PathVariable Long transactionId) throws Exception{
        return ResponseEntity.ok(transactionService.findResponseTransactionById(transactionId, authentication));
    }

    @Operation(method = "PATCH", summary = "editTransactionByAuthUser", description = "Update transaction descriptions.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseTransactionDto> editTransactionByAuthUser(Authentication authentication, @RequestParam(name = "description") String description, @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(transactionService.updateDescriptionFromTransaction(id, authentication, description));
    }

    @Operation(method = "POST", summary = "send money to user", description = "Send Pesos (AR).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/sendArs")
    public ResponseEntity<ResponseTransactionDto> sendArs(Authentication authentication, @RequestParam(name = "amount") Double amount, @RequestParam(name = "description") String description, @RequestParam(name = "receiver account id") Long accountId) throws Exception {
        return ResponseEntity.ok().body(transactionService.send(authentication, amount, description, accountId, ECurrency.ARS));
    }

    @Operation(method = "POST", summary = "send money to user", description = "Send Pesos (USD).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/sendUsd")
    public ResponseEntity<ResponseTransactionDto> sendUsd(Authentication authentication, @RequestParam(name = "amount") Double amount, @RequestParam(name = "description") String description, @RequestParam(name = "receiver account id") Long accountId) throws Exception {
        return ResponseEntity.ok().body(transactionService.send(authentication, amount, description, accountId, ECurrency.USD));
    }
}
