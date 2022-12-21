package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.security.service.IJwtUtils;
import com.alkemy.wallet.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/transactions")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class TransactionController {
    private final ITransactionService transactionService;
    private final IJwtUtils jwtUtils;

    @Operation(method = "POST", summary = "transactionPayment", description = "Registrar un pago.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("payment")
    public ResponseEntity<ResponseTransactionDto> transactionPayment(@RequestBody @Valid TransactionDtoPay transactionDtoPay){
        return new ResponseEntity<>(transactionService.payment(transactionDtoPay), HttpStatus.CREATED);
    }

    @Operation(method = "POST", summary = "saveDeposit", description = "Registrar un depósito.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/deposit")
    public ResponseEntity<ResponseTransactionDto> saveDeposit(
            @RequestBody RequestTransactionDto deposit){
        ResponseTransactionDto depositCreated = transactionService.save(deposit);
        return ResponseEntity.status(HttpStatus.CREATED).body(depositCreated);
    }

    @Operation(method = "GET", summary = "getListTransactionByAdminUser", description = "Traer todas las transacciones de un administrador.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ok"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/list/{userId}")
    public ResponseEntity<Page<Transaction>> getListTransactionByAdminUser(@Parameter(name = "Paginacion", description = "Default Page = 0 , Size = 10", required = false)@PageableDefault(page=0, size=10) Pageable pageable,
                                                                           @Parameter(name = "User Id", description = "User Id (Debe coincidir con el Usuario del Token)", example = "1", required = true)@PathVariable("userId") Long userId,
                                                                           @Parameter(name = "Authorization", description = "Header - Autorization", example = "YOUR_ACCESS_TOKEN", required = true)@RequestHeader(name = "Authorization") String token) throws Exception{
        return ResponseEntity.ok(transactionService.findByUserId(userId, token, pageable));
    }

    @Operation(method = "GET", summary = "getTransactionByAuthUser", description = "Get transaction by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTransactionDto> getTransactionByAuthUser(HttpServletRequest req, @PathVariable Long id) throws Exception{
        return ResponseEntity.ok(transactionService.findResponseTransactionById(id, jwtUtils.getJwt(req.getHeader("Authorization"))));
    }

    @Operation(method = "PATCH", summary = "editTransactionByAuthUser", description = "Update transaction descriptions.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseTransactionDto> editTransactionByAuthUser(HttpServletRequest req, @RequestBody PatchTransactionDescriptionDto description, @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(transactionService.updateDescriptionFromTransaction(id, jwtUtils.getJwt(req.getHeader("Authorization")), description.getDescription()));
    }

    @Operation(method = "POST", summary = "send money to user", description = "Send Pesos (AR).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/sendArs")
    public ResponseEntity<ResponseTransactionDto> sendArs(HttpServletRequest req, @RequestBody RequestSendARTransactionDto requestTransactionDto) {
        String token = req.getHeader("Authorization");
        Long senderId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
        return ResponseEntity.ok().body(transactionService.send(senderId, requestTransactionDto, ECurrency.ARS));
    }

    @Operation(method = "POST", summary = "send money to user", description = "Send Pesos (USD).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/sendUsd")
    public ResponseEntity<ResponseTransactionDto> sendUsd(HttpServletRequest req, @RequestBody RequestSendUSDTransactionDto requestTransactionDto) {
        String token = req.getHeader("Authorization");
        Long senderId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
        return ResponseEntity.ok().body(transactionService.send(senderId, requestTransactionDto, ECurrency.USD));
    }
}
