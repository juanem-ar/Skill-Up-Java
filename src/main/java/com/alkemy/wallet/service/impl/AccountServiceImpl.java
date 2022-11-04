package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.exceptions.ErrorEnum;
import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.mapper.IAccountMapper;
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
    @Autowired
    private IAccountRepository iAccountRepository;
    @Autowired
    private IAccountMapper iAccountMapper;

    @Autowired
    public AccountServiceImpl(IAccountRepository iAccountRepository) {
        this.iAccountRepository = iAccountRepository;
    }

    @Override
    public List<ResponseAccountDto> findAllByUser(Optional<User> user)  {
 //           if(user.isEmpty())
        //              throw new ResourceNotFoundException("User Not Found");
        //        return new ResponseEntity<>( , HttpStatus.NOT_FOUND);
        return iAccountMapper.accountsToAccountsDto(user.get().getAccounts());
    }

    @Override
    public Optional<Account> findById(Long id) {
        return iAccountRepository.findById(id);
    }
}
