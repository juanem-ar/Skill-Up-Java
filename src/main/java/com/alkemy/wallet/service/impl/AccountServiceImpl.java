package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.CurrencyDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.alkemy.wallet.model.ECurrency.ARS;
import static com.alkemy.wallet.model.ECurrency.USD;

@Service
public class AccountServiceImpl implements IAccountService {
    public static final Double LIMIT_ARS = 300000.00;
    public static final Double LIMIT_USD = 1000.00;
    private IAccountRepository iAccountRepository;
    private JwtUtils jwtUtils;
    private IUserRepository userRepository;
    private IUserService iUserService;
    @Autowired
    public AccountServiceImpl( IAccountRepository iAccountRepository, IUserService iUserService , JwtUtils jwtUtils, IUserRepository userRepository) {
        this.iAccountRepository = iAccountRepository;
        this.iUserService = iUserService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }
    @Override
    public List<Account> findAllByUser(User user) {
        return user.getAccounts();
    }
    @Override
    public void addAccount(String email, CurrencyDto currency) throws Exception {
        try{
            if(this.iAccountRepository.countByUserId(this.userRepository.findByEmail(email).getId()) <= 1) {
                User userLogged = this.userRepository.findByEmail(email);
                Account account = createAccount(currency);
                account.setUser(userLogged);
                this.iAccountRepository.save(account);
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            throw new BadRequestException("There is an " + currency.getCurrency() + " account with that email adress: " + email);
        }
    }
    @Override
    public Account createAccount(CurrencyDto currency) {
        Account account = new Account();
        ECurrency currencyDto = currency.getCurrency();
        System.out.println(currencyDto);
        if(currencyDto == ARS){
            account.setCurrency(ARS);
            account.setTransactionLimit(LIMIT_ARS);
        }else{
            account.setCurrency(USD);
            account.setTransactionLimit(LIMIT_USD);
        }
        account.setDeleted(false);
        account.setBalance(0.00);
        return account;
    }
}
