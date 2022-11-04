package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/accounts")
@RestController
public class AccountController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IAccountService iAccountService;

    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping("{id}")
    public ResponseEntity<List<ResponseAccountDto>> listAccountsByUser(@PathVariable Long id){
        return new ResponseEntity<>(iAccountService.findAllByUser(id), HttpStatus.OK);
    }
    @Secured(value = { "ROLE_USER" })
    @PatchMapping("{:id}")
    public ResponseEntity<ResponseAccountDto> updateAccount(@PathVariable Long id, Authentication authentication, @RequestParam Double transactionLimit){
        return new ResponseEntity<>(iAccountService.updateAccount(id,transactionLimit,authentication), HttpStatus.OK);
    }
}
