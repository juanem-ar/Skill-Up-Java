package com.alkemy.wallet.controller;

import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RequestMapping("api/v1/accounts")
@RestController
public class AccountController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountMapper iAccountMapper;

    @GetMapping("{id}")
    public ResponseEntity<Object> listAccountsByUser(@PathVariable Long id){
        Optional<User> user = userService.findById(id);

        if(user.isEmpty())
            return new ResponseEntity<>( "User Not Found", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(iAccountMapper.accountsToAccountsDto(accountService.findAllByUser(user.get())), HttpStatus.OK);
    }
}
