package com.alkemy.wallet.service;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    List<Account> findAllByUser(User user);

    Optional<Account> findById(Long id);
}
