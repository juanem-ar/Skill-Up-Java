package com.alkemy.wallet.controller;


import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.security.service.IJwtUtils;
import com.alkemy.wallet.service.impl.TransactionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("api/v1/transactions")
@RestController

public class TransactionController {

    private final TransactionServiceImpl transactionService;
    private final IJwtUtils jwtUtils;

    @Autowired
    public TransactionController(TransactionServiceImpl transactionService, IJwtUtils jwtUtils) {
        this.transactionService = transactionService;
        this.jwtUtils = jwtUtils;
    }

    @Operation(method = "POST", summary = "transactionPayment", description = "Registrar un pago.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("payment")
    public ResponseEntity<ResponseTransactionDto> transactionPayment(@RequestBody @Valid ResponseTransactionDto responseTransactionDto){
        return new ResponseEntity<>(transactionService.payment(responseTransactionDto), HttpStatus.CREATED);
    }

    @Operation(method = "POST", summary = "saveDeposit", description = "Registrar un depósito.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/deposit")
    public ResponseEntity<ResponseTransactionDto> saveDeposit(
            @RequestBody ResponseTransactionDto deposit){
        ResponseTransactionDto depositCreated = transactionService.save(deposit);
        return ResponseEntity.status(HttpStatus.CREATED).body(depositCreated);
    }

    @Operation(method = "GET", summary = "getListTransactionByAdminUser", description = "Traer todas las transacciones de un administrador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping(value = "/{userId}")
    public ResponseEntity<Page<Transaction>> getListTransactionByAdminUser(@PageableDefault(page=0, size=10) Pageable pageable,
                                                                           @PathVariable("userId") Long userId,
                                                                           @RequestHeader(name = "Authorization") String token) throws Exception{
        return ResponseEntity.ok(transactionService.findByUserId(userId, token, pageable));
    }

    @Operation(method = "GET", summary = "getTransactionByAuthUser", description = "Traer todas las transacciones de un usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("transaction/{transactionId}")
    public ResponseEntity<?> getTransactionByAuthUser(@PathVariable("transactionId") Long id,
                                                      @RequestHeader(name = "Authorization") String token) throws Exception{
        return ResponseEntity.ok(transactionService.findTransactionById(id, token));

    }

    @Operation(method = "PATCH", summary = "editTransactionByAuthUser", description = "Actualizar transacción.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PatchMapping("{transactionId}")
    public ResponseEntity<?> editTransactionByAuthUser(@RequestBody Map<Object, String> description,
                                                       @RequestHeader(name = "Authorization") String token,
                                                       @PathVariable("transactionId") Long id) throws Exception {
        return ResponseEntity.ok(transactionService.updateDescriptionFromTransaction(id, token, description.get("description")));
    }

    @PostMapping("/sendArs/{id}")
    public ResponseEntity<TransactionDtoPay> sendArs(HttpServletRequest req, @PathVariable("id") Long accountId, Double amount) {
        String token = req.getHeader("Authorization");
        Long senderId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
        return ResponseEntity.ok().body(transactionService.sendArs(senderId,accountId,amount));
    }

    @PostMapping("/sendUsd/{id}")
    public ResponseEntity<TransactionDtoPay> sendUsd(HttpServletRequest req, @PathVariable("id") Long accountId, Double amount) {
        String token = req.getHeader("Authorization");
        Long senderId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
        return ResponseEntity.ok().body(transactionService.sendUsd(senderId,accountId,amount));
    }
}
