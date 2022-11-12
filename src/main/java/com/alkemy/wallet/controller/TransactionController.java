package com.alkemy.wallet.controller;


import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.dto.ResponseSendTransactionDto;
import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.security.service.IJwtUtils;
import com.alkemy.wallet.service.impl.TransactionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
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

@RequestMapping("/transactions")
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServiceImpl transactionService;
    private final IJwtUtils jwtUtils;

    @Operation(method = "POST", summary = "transactionPayment", description = "Registrar un pago.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("payment")
    public ResponseEntity<ResponseTransactionDto> transactionPayment(@RequestBody @Valid TransactionDtoPay transactionDtoPay){
        return new ResponseEntity<>(transactionService.payment(transactionDtoPay), HttpStatus.CREATED);
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
    public ResponseEntity<Page<Transaction>> getListTransactionByAdminUser(@Parameter(name = "Paginacion", description = "Default Page = 0 , Size = 10", required = false)@PageableDefault(page=0, size=10) Pageable pageable,
                                                                           @Parameter(name = "User Id", description = "User Id (Debe coincidir con el Usuario del Token)", example = "1", required = true)@PathVariable("userId") Long userId,
                                                                           @Parameter(name = "Authorization", description = "Header - Autorization", example = "YOUR_ACCESS_TOKEN", required = true)@RequestHeader(name = "Authorization") String token) throws Exception{
        return ResponseEntity.ok(transactionService.findByUserId(userId, token, pageable));
    }

    @Operation(method = "GET", summary = "getTransactionByAuthUser", description = "Traer todas las transacciones de un usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("transaction/{transactionId}")
    public ResponseEntity<ResponseTransactionDto> getTransactionByAuthUser(@Parameter(name = "Transaction Id", description = "Id de transferencia que se desea obtener", example = "9", required = true)@PathVariable("transactionId") Long id,
                                                                           @Parameter(name = "Authorization", description = "Header - Autorization", example = "YOUR_ACCESS_TOKEN", required = true)@RequestHeader(name = "Authorization") String token) throws Exception{
        return ResponseEntity.ok(transactionService.findTransactionById(id, token).get());

    }

    @Operation(method = "PATCH", summary = "editTransactionByAuthUser", description = "Actualizar transacción.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PatchMapping("{transactionId}")
    public ResponseEntity<ResponseTransactionDto> editTransactionByAuthUser(@Parameter(name = "Descripcion", description = "String enviado por body para modificar la descripcion", schema = @Schema(example = "{\"description\" : \"String\"}"), required = true)@RequestBody Map<Object, String> description,
                                                                            @Parameter(name = "Authorization", description = "Header - Autorization", example = "YOUR_ACCESS_TOKEN", required = true)@RequestHeader(name = "Authorization") String token,
                                                                            @Parameter(name = "Transaction Id", description = "Id de transferencia que se desea Modificar", example = "9", required = true)@PathVariable("transactionId") Long id) throws Exception {
        return ResponseEntity.ok(transactionService.updateDescriptionFromTransaction(id, token, description.get("description")));
    }

    @Operation(method = "POST", summary = "sendArs", description = "Send pesos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/sendArs")
    public ResponseEntity<ResponseTransactionDto> sendArs(HttpServletRequest req, ResponseSendTransactionDto responseSendTransactionDto) {
        String token = req.getHeader("Authorization");
        Long senderId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
        return ResponseEntity.ok().body(transactionService.send(senderId, responseSendTransactionDto, ECurrency.ARS));
    }

    @Operation(method = "POST", summary = "sendUsd", description = "Send dollars.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/sendUsd")
    public ResponseEntity<ResponseTransactionDto> sendUsd(HttpServletRequest req, ResponseSendTransactionDto responseSendTransactionDto) {
        String token = req.getHeader("Authorization");
        Long senderId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
        return ResponseEntity.ok().body(transactionService.send(senderId, responseSendTransactionDto, ECurrency.USD));
    }
}
