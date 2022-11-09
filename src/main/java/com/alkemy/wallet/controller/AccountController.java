package com.alkemy.wallet.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import com.alkemy.wallet.dto.CurrencyDto;
import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.dto.ResponseUserBalanceDto;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/accounts")
@RestController
public class AccountController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IAccountService iAccountService;

    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping("{id}")
    public ResponseEntity<List<ResponseAccountDto>> listAccountsByUser(@PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(iAccountService.findAllByUser(id), HttpStatus.OK);
    }
    @Secured(value = { "ROLE_USER" })
    @PatchMapping("{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long id, @RequestHeader(name = "Authorization") String token, @RequestBody UpdateAccountDto requestAccountDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(iAccountService.updateAccount(id,requestAccountDto,token), HttpStatus.OK);
    }
    @GetMapping("/balance")
	public ResponseEntity<
		ResponseUserBalanceDto> getAccountsBalance(
			@RequestHeader(name = "Authorization") String token) {
		return ResponseEntity.ok(iAccountService.getBalance(token));
	}
    @PostMapping
    public ResponseEntity<String> createAccount(HttpServletRequest req, @Valid @RequestBody CurrencyDto currency) throws Exception {
        String token = req.getHeader("Authorization");
        String userEmail = jwtUtils.extractUsername(jwtUtils.getJwt(token));
        return ResponseEntity.status(HttpStatus.OK).body(iAccountService.addAccount(userEmail, currency));
    }
}
