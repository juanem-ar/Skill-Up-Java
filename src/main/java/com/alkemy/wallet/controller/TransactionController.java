package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.security.service.IJwtUtils;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final IJwtUtils jwtUtils;

    /*
    @Operation(method = "POST", summary = "transactionPayment", description = "Registrar un pago.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("payment")
    public ResponseEntity<ResponseTransactionDto> transactionPayment(HttpServletRequest req, @RequestBody @Valid TransactionDtoPay transactionDtoPay) throws Exception {
        return new ResponseEntity<>(transactionService.payment(transactionDtoPay, jwtUtils.getJwt(req.getHeader("Authorization"))), HttpStatus.CREATED);
    }
    */

    @Operation(method = "POST", summary = "saveDeposit", description = "Registrar un depósito.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/deposit")
    public ResponseEntity<ResponseTransactionDto> saveDeposit(
            HttpServletRequest req,
            @RequestBody RequestTransactionDto deposit){
        ResponseTransactionDto depositCreated = transactionService.save(deposit, jwtUtils.getJwt(req.getHeader("Authorization")));
        return ResponseEntity.status(HttpStatus.CREATED).body(depositCreated);
    }

    @Operation(method = "GET", summary = "getListTransactionByUserLogged", description = "Get all transactions from user logged.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping
    public ResponseEntity<List<ResponseTransactionDto>> getListTransactionByUserLogged(HttpServletRequest req) throws Exception{
        return ResponseEntity.ok(transactionService.findAllTransactionsByUserId(jwtUtils.getJwt(req.getHeader("Authorization"))));
    }

    @Operation(method = "GET", summary = "transactions pagination by admin", description = "Order by page with five elements and sorted by account ascending and amount descending",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionPageDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/list")
    public ResponseEntity<TransactionPageDto> getAllTransactionPages(@RequestParam(value = "page", defaultValue = "1") @PathVariable int page) throws Exception{
        return ResponseEntity.ok(transactionService.findAllByAccount(page));
    }

    @Operation(method = "GET", summary = "getTransactionByIdAndUserLogged", description = "Get transaction by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{transactionId}")
    public ResponseEntity<ResponseTransactionDto> getTransactionByIdAndUserLogged(HttpServletRequest req, @PathVariable Long transactionId) throws Exception{
        return ResponseEntity.ok(transactionService.findResponseTransactionById(transactionId, jwtUtils.getJwt(req.getHeader("Authorization"))));
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
    public ResponseEntity<ResponseTransactionDto> sendArs(HttpServletRequest req, @RequestBody RequestSendARTransactionDto requestTransactionDto) throws Exception{

        return ResponseEntity.ok().body(transactionService.send(jwtUtils.getJwt(req.getHeader("Authorization")), requestTransactionDto, ECurrency.ARS));
    }

    @Operation(method = "POST", summary = "send money to user", description = "Send Pesos (USD).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTransactionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/sendUsd")
    public ResponseEntity<ResponseTransactionDto> sendUsd(HttpServletRequest req, @RequestBody RequestSendUSDTransactionDto requestTransactionDto) throws Exception{
        return ResponseEntity.ok().body(transactionService.send(jwtUtils.getJwt(req.getHeader("Authorization")), requestTransactionDto, ECurrency.USD));
    }
}
