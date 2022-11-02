package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.EType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("TRANSACTIONS")
public class TransactionController {

    @Lazy
    @Autowired

    // Especificar id de cuenta destinataria, monto y todos los campos NOT NULL de la tabla Transactions
    @PostMapping("/transactions/")
    public ResponseEntity<TransactionDTO> sendArs(@PathVariable Long accountId, Long amount, EType type) {
        //try - catch
    }

    // Especificar el id de la cuenta destinataria, el monto, y todos los campos NOT NULL de la tabla Transactions
    public ResponseEntity<TransactionDTO> sendUsd(@PathVariable Long accountId, Long amount, EType type) {
        //try - catch
    }
}

