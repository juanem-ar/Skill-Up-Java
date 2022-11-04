package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.ResponseAccountDto;
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
        Optional<User> user = iUserService.findById(id);
        //           if(user.isEmpty())
        //              throw new ResourceNotFoundException("User Not Found");
        //        return new ResponseEntity<>( , HttpStatus.NOT_FOUND);
        return iAccountMapper.accountsToAccountsDto(user.get().getAccounts());
    }

    @Override
    public Optional<Account> findById(Long id) {
        return iAccountRepository.findById(id);
    }

    @Override
    public ResponseAccountDto updateAccount(Long id, Map<String,Double> requestAccount, Authentication authentication){
        Optional<Account> account = this.findById(id);

        //if (account.isEmpty())
        //return new ResponseEntity<>( "Account Not Found", HttpStatus.NOT_FOUND);
        //if logged user is not the same as who is updating the account
        //Unauthorized

        //if (account.get().getId().equals(authentig))
        account.get().setTransactionLimit(requestAccount.get("transactionLimit"));
        return iAccountMapper.accountToAccountDto(iAccountRepository.save(account.get()));
    }
}
