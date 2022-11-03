package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private JwtUtils jwtUtils;
    private AuthenticationResponseDto authenticationResponseDto;

    @Autowired
    private AccountRepository accountRepository;

    /*
    @PostMapping("/sendArs")
    public ResponseEntity<TransactionDto> sendArs(@PathVariable Long accountId, Long amount, EType type) {
        Long senderId = jwtUtils.extractUserId(authenticationResponseDto.getJwt());
        return ResponseEntity.ok().body(transactionService.sendArs(senderId,accountId,amount,type));
    }*/
}

