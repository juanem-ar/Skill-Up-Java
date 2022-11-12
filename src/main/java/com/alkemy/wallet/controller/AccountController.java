package com.alkemy.wallet.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.alkemy.wallet.dto.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import com.alkemy.wallet.dto.UpdateAccountDto;
import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/accounts")
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final JwtUtils jwtUtils;

    private final IUserService iUserService;
    private final IAccountService iAccountService;

    @Operation(method = "GET", summary = "listAccountsByUser", description = "Listar todas las cuentas de un Usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping("{id}")
    public ResponseEntity<List<ResponseAccountDto>> listAccountsByUser(@PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(iAccountService.findAllByUser(id), HttpStatus.OK);
    }


    @Operation(method = "GET", summary = "findAllAccounts", description = "Traer todas las cuentas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    //@Secured(value = { "ROLE_ADMIN" })
    @GetMapping
    public ResponseEntity<ResponseAccountsDto> findAllAccounts(
            @RequestParam(required = false, name = "page") Integer page,
            HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(iAccountService.findAll(
                page,
                httpServletRequest));
    }

    @Operation(method = "PATCH", summary = "updateAccount", description = "Actualizar una cuenta.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente", content = @Content(schema = @Schema(implementation = ResponseAccountDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @Secured(value = { "ROLE_USER" })
    @PatchMapping("{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long id, @RequestHeader(name = "Authorization") String token, @RequestBody UpdateAccountDto requestAccountDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(iAccountService.updateAccount(id,requestAccountDto,token), HttpStatus.OK);
    }

    @Operation(method = "GET", summary = "getAccountBalance", description = "Obtener el balance de ambas cuentas de un usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/balance")
	public ResponseEntity<
		ResponseUserBalanceDto> getAccountsBalance(
			@RequestHeader(name = "Authorization") String token) {
		return ResponseEntity.ok(iAccountService.getBalance(token));
	}

    @Operation(method = "POST", summary = "createAccount", description = "Crear una cuenta.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok. El recurso se obtiene correctamente"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error inesperado del sistema", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping
    public ResponseEntity<String> createAccount(HttpServletRequest req, @Valid @RequestBody CurrencyDto currency) throws Exception {
        String token = req.getHeader("Authorization");
        String userEmail = jwtUtils.extractUsername(jwtUtils.getJwt(token));
        return ResponseEntity.status(HttpStatus.OK).body(iAccountService.addAccount(userEmail, currency));
    }
}
