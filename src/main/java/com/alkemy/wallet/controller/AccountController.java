package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.User;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequestMapping("api/v1/accounts")
@RestController
public class AccountController {
    private IUserService userService;
    private IAccountService accountService;

    @GetMapping("{id}")
    public ResponseEntity<Object> listAccountsByUser(@PathVariable Long id){
        Optional<User> user = userService.findById(id);
        Map<String,Object> responseMap = new HashMap<String, Object>();

        if(user.isEmpty())
            return new ResponseEntity<>( responseMap.put("error","User Not Found"), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>( responseMap.put("Accounts:",accountService.findAllByUser(user.get())), HttpStatus.OK);
    }
}
