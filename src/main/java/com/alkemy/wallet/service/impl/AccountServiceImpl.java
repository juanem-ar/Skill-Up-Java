package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements IAccountService {
    private IAccountRepository iAccountRepository;

    @Autowired
    public AccountServiceImpl(IAccountRepository iAccountRepository) {
        this.iAccountRepository = iAccountRepository;
    }

    @Override
    public List<Account> findAllByUser(User user) {
        return user.getAccounts();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return iAccountRepository.findById(id);
    }
}
