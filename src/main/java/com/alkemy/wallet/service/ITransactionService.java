package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDtoPay;

public interface ITransactionService {
    public TransactionDtoPay payment(TransactionDtoPay transitionDtoPay);
}
