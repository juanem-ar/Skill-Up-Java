package com.alkemy.wallet.controller;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;


@RequestMapping("/accounts")
@RestController
public class AccountController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountMapper iAccountMapper;

    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping("{id}")
    public ResponseEntity<List<ResponseAccountDto>> listAccountsByUser(@PathVariable Long id){
        Optional<User> user = userService.findById(id);
    //    if(user.isEmpty())
    //        return new ResponseEntity<>( "User Not Found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(iAccountMapper.accountsToAccountsDto(accountService.findAllByUser(user.get())), HttpStatus.OK);
    }

    
    @GetMapping("/balance")
	public ResponseEntity<
		ResponseUserBalanceDto> getAccountsBalance(
			@RequestHeader(name = "Authorization") String token) {
		return ResponseEntity.ok(accountService.getBalance(token));
	}

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestHeader("Authorization") String token, @Valid @RequestBody CurrencyDto currency) throws Exception {
        accountService.addAccount(jwtUtils.extractUsername(jwtUtils.getJwt(token)), currency);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
