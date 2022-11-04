package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDtoPay;
import com.alkemy.wallet.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("api/v1/transactions")
@RestController
public class TransactionController {

    private final TransactionServiceImpl transactionService;
    @Autowired
    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("payment")
    public ResponseEntity<TransactionDtoPay>  transactionPayment(@RequestBody @Valid TransactionDtoPay transactionDtoPay){
        return new ResponseEntity<>(transactionService.payment(transactionDtoPay), HttpStatus.CREATED);
    }

}
