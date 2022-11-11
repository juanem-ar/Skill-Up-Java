package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exceptions.BadRequestException;
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



import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.service.ITransactionService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.alkemy.wallet.model.ECurrency.ARS;
import static com.alkemy.wallet.model.ECurrency.USD;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    public static final Double LIMIT_ARS = 300000.00;
    public static final Double LIMIT_USD = 1000.00;
    private final IAccountRepository iAccountRepository;
    private final IJwtUtils jwtUtils;
    private final IUserRepository userRepository;
    private final IUserService iUserService;
    private final ITransactionService transactionService;
    private final IAccountMapper accountMapper;
    private static final Integer ACCOUNTSFORPAGE = 10;


    @Override
    public List<ResponseAccountDto> findAllByUser(Long id)  {
        User user = iUserService.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found User with number id: "+ id));
        //return accountMapper.accountsToAccountsDto(user.getAccounts()); TODO if findAll function is correct, delete this line
        return accountMapper.accountsToAccountsDto(iAccountRepository.findAllByUserId(user.getId()));
    }

    @Override
    public Optional<Account> findById(Long id) {
        return iAccountRepository.findById(id);
    }

    @Override
    public ResponseAccountDto updateAccount(Account account, UpdateAccountDto requestAccount, Authentication authentication){
        account.setTransactionLimit(requestAccount.getTransactionLimit());
        return accountMapper.accountToAccountDto(iAccountRepository.save(account));
    }

	@Override
	public ResponseUserBalanceDto getBalance(String token) {
		Long userId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
		//User user = iUserService.getUserById(userId); TODO if foreach function is correct, delete this line
		ResponseUserBalanceDto dto = new ResponseUserBalanceDto();
		dto.setId(userId);
		for(Account account : iAccountRepository.findAllByUserId(userId)) {
			AccountBalanceDto balanceDto = accountMapper.accountToBalanceDto(account);
			balanceDto.setBalance(
				calcularBalance(
					account.getBalance(),
					transactionService.findAllTransactionsWith(account.getId())));
			dto.getAccountBalanceDtos().add(balanceDto);
		}
		return dto;
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
    public String addAccount(String email, CurrencyDto currency) throws Exception {
        int countByUserId = iAccountRepository.countByUserId(userRepository.findByEmail(email).getId()).intValue();
        if (countByUserId < 0 || countByUserId >1){
            throw new ResourceNotFoundException("You have 2 associated accounts");
        }else{
            if (iAccountRepository.getReferenceByUserId(userRepository.findByEmail(email).getId()) != null) {
                Account accRegistered = iAccountRepository.getReferenceByUserId(userRepository.findByEmail(email).getId());
                if (accRegistered.getCurrency() == currency.getCurrency()) {
                    throw new ResourceNotFoundException("You already have an " + currency.getCurrency() + " associated account.");
                }
            }
            Account account = createAccount(currency);
            User userLogged = userRepository.findByEmail(email);
            account.setUser(userLogged);
            iAccountRepository.save(account);
            return HttpStatus.CREATED.getReasonPhrase();
        }
    }

    @Override
    public Account createAccount(CurrencyDto currency) {
        Account account = new Account();
        ECurrency currencyDto = currency.getCurrency();
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

    @Override
    public ResponseAccountsDto findAll(Integer page, HttpServletRequest httpServletRequest) {
        ResponseAccountsDto dto = new ResponseAccountsDto();
        if (page == null) {
            dto.setAccountsDto(
                    accountMapper.accountsToAccountsDto(
                            iAccountRepository.findAll()));
            return dto;
        }

        Page<Account> accounts = iAccountRepository.findAll(
                PageRequest.of(page, ACCOUNTSFORPAGE));

        if (accounts.isEmpty())
            throw new BadRequestException();

        dto.setAccountsDto(
                accountMapper.accountsToAccountsDto(
                        accounts.toList()));

        // url
        String url = httpServletRequest
                .getRequestURL().toString() + "?" + "page=";

        if(accounts.hasPrevious()) {
            int previousPage = accounts.getNumber() - 1;
            dto.setPreviousPage(url + previousPage);
        }

        if (accounts.hasNext()) {
            int nextPage = accounts.getNumber() + 1;
            dto.setNextpage(url + nextPage);
        }
        return dto;
    }
}
