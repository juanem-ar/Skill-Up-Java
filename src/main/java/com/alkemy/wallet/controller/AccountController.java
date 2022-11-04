package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("api/v1/accounts")
@RestController
public class AccountController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccountService accountService;

    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping("{id}")
    public ResponseEntity<List<ResponseAccountDto>> listAccountsByUser(@PathVariable Long id){
        return new ResponseEntity<>(accountService.findAllByUser(userService.findById(id)), HttpStatus.OK);
    }
    @Secured(value = { "ROLE_USER" })
    @PatchMapping("{:id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long id, Authentication authentication, @RequestParam Double transactionLimit){
        Optional<Account> account = accountService.findById(id);

        //if (account.isEmpty())
            return new ResponseEntity<>( "Account Not Found", HttpStatus.NOT_FOUND);
        //if (authentication.)

        //account.get().setTransactionLimit(transactionLimit);

        //return new ResponseEntity<>(accountService.updateAccount(account,transactionLimit), HttpStatus.OK);
    }
}
