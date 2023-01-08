package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.ResponseSimulatedFixedDepositDto;
import com.alkemy.wallet.service.IFixedDepositService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RequestMapping("/fixedDeposit")
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "FixedDeposit", description = "Create and simulate fixed deposit")
public class FixedTermDepositController {
    private final IFixedDepositService fixedDepositService;
    @Operation(method = "POST",summary = "createFixedDeposit", description = "Create fixed deposit by user authenticated.", responses = {
            @ApiResponse(responseCode = "200", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(example = "Created fixed deposit (ID: number )"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<String> createFixedDeposit(
            @Valid Authentication authentication, @RequestParam(name = "amount") Double amount, @RequestParam(name = "currency") String currency, @RequestParam(name = "period") int period) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(fixedDepositService.addFixedDeposit(authentication, amount, currency, period));
    }

    @Operation(method = "POST", summary = "simulateFixedDeposit", description = "Simulate fixed deposit by user authenticated.", responses = {
            @ApiResponse(responseCode = "200", description = "Ok",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseSimulatedFixedDepositDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/simulate")
    public ResponseEntity<ResponseSimulatedFixedDepositDto> simulateFixedDeposit(@Valid @RequestParam(name = "amount") Double amount, @RequestParam(name = "currency") String currency, @RequestParam(name = "period") int period) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(fixedDepositService.simulateFixedDeposit(currency,period,amount));
    }
}
