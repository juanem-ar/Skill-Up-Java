package com.alkemy.wallet.controller;


import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import javax.validation.Valid;

@RequestMapping("api/v1/transactions")
@RestController

public class TransactionController {

    private final TransactionServiceImpl transactionService;
    private final JwtUtils jwtUtils;

    @Autowired
    public TransactionController(TransactionServiceImpl transactionService, JwtUtils jwtUtils) {
        this.transactionService = transactionService;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("payment")
    public ResponseEntity<TransactionDtoPay>  transactionPayment(@RequestBody @Valid TransactionDtoPay transactionDtoPay){
        return new ResponseEntity<>(transactionService.payment(transactionDtoPay), HttpStatus.CREATED);
    }


    @PostMapping("/deposit")
    public ResponseEntity<ResponseTransactionDto> saveDeposit(
            @RequestBody ResponseTransactionDto deposit){
        ResponseTransactionDto depositCreated = transactionService.save(deposit);
        return ResponseEntity.status(HttpStatus.CREATED).body(depositCreated);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getListTransactionByAdminUser(@PathVariable("userId") Long userId) {
        //IF PARA VALIDAR USUARIO ADMINISTRADOR
        List<ResponseTransactionDto> listTransactionsByUser = transactionService.findByUserId(userId);
        if (listTransactionsByUser.isEmpty()) {
            return new ResponseEntity<>("User doesn't exist or has not transactions", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(listTransactionsByUser, HttpStatus.OK);
        }
    }



}
