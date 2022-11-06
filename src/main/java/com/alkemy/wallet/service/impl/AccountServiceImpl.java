package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private IAccountRepository iAccountRepository;
    @Autowired
    private IAccountMapper iAccountMapper;

    @Autowired
    private IUserService iUserService;
    @Autowired
    public AccountServiceImpl(IAccountRepository iAccountRepository) {
        this.iAccountRepository = iAccountRepository;
    }

    @Override
    public List<ResponseAccountDto> findAllByUser(Long id)  {
        User user = iUserService.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found User with number id: "+ id));
        return iAccountMapper.accountsToAccountsDto(user.getAccounts());
    }

    @Override
    public Optional<Account> findById(Long id) {
        return iAccountRepository.findById(id);
    }

    @Override
    //todo change requestAccount for Dto
    public ResponseAccountDto updateAccount(Account account, Map<String,Double> requestAccount, Authentication authentication){
        account.setTransactionLimit(requestAccount.get("transactionLimit"));
        return iAccountMapper.accountToAccountDto(iAccountRepository.save(account));
    }
}
