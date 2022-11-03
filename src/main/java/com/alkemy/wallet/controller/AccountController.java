package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequestMapping("api/v1/accounts")
@RestController
public class AccountController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccountService accountService;

    @GetMapping("{id}")
    public ResponseEntity<Object> listAccountsByUser(@PathVariable Long id){
        Optional<User> user = userService.findById(id);
        Map<String,Object> responseMap = new HashMap<String, Object>();

        if(user.isEmpty())
            return new ResponseEntity<>( responseMap.put("error","User Not Found"), HttpStatus.NOT_FOUND);
        responseMap = AccountDto.accountsToDto(accountService.findAllByUser(user.get()));
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
