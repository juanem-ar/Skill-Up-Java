package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.CurrencyDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.IJwtUtils;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.dto.AccountBalanceDto;
import com.alkemy.wallet.dto.ResponseUserBalanceDto;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.service.ITransactionService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.alkemy.wallet.model.ECurrency.ARS;
import static com.alkemy.wallet.model.ECurrency.USD;

@Service
public class AccountServiceImpl implements IAccountService {
    public static final Double LIMIT_ARS = 300000.00;
    public static final Double LIMIT_USD = 1000.00;
    private IAccountRepository iAccountRepository;
    private IJwtUtils jwtUtils;
    private IUserRepository userRepository;
    private IUserService iUserService;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IAccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl( IAccountRepository iAccountRepository, IUserService iUserService , IJwtUtils jwtUtils, IUserRepository userRepository) {
        this.iAccountRepository = iAccountRepository;
        this.iUserService = iUserService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }
    @Override
    public List<ResponseAccountDto> findAllByUser(Long id)  {
        User user = iUserService.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found User with number id: "+ id));
        return accountMapper.accountsToAccountsDto(user.getAccounts());
    }

    @Override
    public Optional<Account> findById(Long id) {
        return iAccountRepository.findById(id);
    }

    @Override
    //todo change requestAccount for Dto
    public ResponseAccountDto updateAccount(Account account, Map<String,Double> requestAccount, Authentication authentication){
        account.setTransactionLimit(requestAccount.get("transactionLimit"));
        return accountMapper.accountToAccountDto(iAccountRepository.save(account));
    }

	@Override
	public ResponseUserBalanceDto getBalance(String token) {
		Long userId = jwtUtils.extractUserId(token);

		User user = iUserService.getUserById(userId);

		ResponseUserBalanceDto responseUserBalanceDto = new ResponseUserBalanceDto();
		responseUserBalanceDto.setId(userId);

		for(Account account : user.getAccounts()) {
			AccountBalanceDto accountBalanceDto = accountMapper.accountToBalanceDto(account);

			accountBalanceDto.setBalance(
				calcularBalance(
					account.getBalance(),
					transactionService.findAllTransactionsWith(account.getId())));

			responseUserBalanceDto.getAccountBalanceDtos().add(accountBalanceDto);
		}

		return responseUserBalanceDto;
	}


	private Double calcularBalance(
		Double balanceBase, List<Transaction> transactions) {
		Double b = balanceBase;

		for(Transaction transaction : transactions) {
			if(transaction.getType() == EType.DEPOSIT
				|| transaction.getType() == EType.INCOME)
				b += transaction.getAmount();
			else if(transaction.getType() == EType.PAYMENT)
				b -= transaction.getAmount();
		}

		return b;
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
