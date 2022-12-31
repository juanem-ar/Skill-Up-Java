package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.exceptions.TransactionError;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.mapper.IFixedTermDepositMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IFixedTermDepositRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.model.FixedTermDeposit;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import static com.alkemy.wallet.model.ECurrency.ARS;
import static com.alkemy.wallet.model.ECurrency.USD;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {
    public static final Double LIMIT_ARS = 300000.00;
    public static final Double LIMIT_USD = 1000.00;
    private static final Integer ACCOUNTS_FOR_PAGE = 10;
    private final IAccountRepository iAccountRepository;
    private final IUserRepository userRepository;
    private final IUserService iUserService;
    private final IAccountMapper accountMapper;
    private final IFixedTermDepositRepository iFixedTermDepositRepository;
    private final IFixedTermDepositMapper iFixedTermDepositMapper;

    @Override
    public List<ResponseAccountDto> findAllByUser(Long id)  {
        User user = iUserService.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found User with number id: "+ id));
        return accountMapper.accountsToAccountsDto(iAccountRepository.findAllByUserId(user.getId()));
    }

    @Override
    public ResponseAccountDto updateAccount(Long id, Double limit, Authentication authentication) throws Exception {
        Account account = iAccountRepository.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found Account with number id: "+ id));
        if (!account.getUser().getEmail().equals(authentication.getName()))
            throw new ResourceNotFoundException("You don't have permission to access this resource");
        if (limit < 0)
            throw new BadRequestException("The limit must be greater than or equal to 0.");
        account.setTransactionLimit(limit);
        return accountMapper.accountToAccountDto(iAccountRepository.save(account));
    }

	@Override
	public ResponseUserBalanceDto getBalance(Authentication authentication) {
		User user = userRepository.findByEmail(authentication.getName());
		ResponseUserBalanceDto dto = new ResponseUserBalanceDto();

        List<Account> listAccounts = iAccountRepository.findAllByUserId(user.getId());
        dto.setAccountBalance(accountMapper.accountListToBalanceDtoList(listAccounts));

        Long arsIdAccount = iAccountRepository.getReferenceByUserIdAndCurrency(user.getId(), ARS).getId();
        Long usdIdAccount = iAccountRepository.getReferenceByUserIdAndCurrency(user.getId(), USD).getId();

        List<FixedTermDeposit> listArs = iFixedTermDepositRepository.findAllByAccountId(arsIdAccount);
        List<FixedTermDeposit> listUsd = iFixedTermDepositRepository.findAllByAccountId(usdIdAccount);

		dto.setArsFixedDeposits(iFixedTermDepositMapper.fixedDepositListToResponseList(listArs));
        dto.setUsdFixedDeposits(iFixedTermDepositMapper.fixedDepositListToResponseList(listUsd));
		return dto;
	}

    @Override
    public String addAccount(String email, String currencyParam) throws Exception {
        CurrencyDto currency = new CurrencyDto();
        try{
            currency.setCurrency(ECurrency.valueOf(currencyParam));
        }catch (Exception ex){
            throw new BadRequestException("Insert ARS or USD");
        }
        int countByUserId = iAccountRepository.countByUserId(userRepository.findByEmail(email).getId()).intValue();
        if (countByUserId < 0 || countByUserId >1)
            throw new ResourceNotFoundException("You have 2 associated accounts");
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
    public ResponseAccountsListDto findAll(Integer page, HttpServletRequest httpServletRequest) throws Exception {
        if (page <= 0)
            throw new TransactionError("You request page not found, try page 1");

        Pageable pageWithTenElementsAndSortedByAccountIdAscAndBalanceDesc = PageRequest.of(page-1,ACCOUNTS_FOR_PAGE,
                Sort.by("id")
                        .ascending()
                        .and(Sort.by("balance")
                                .descending()));
        Page<Account> accounts = iAccountRepository.findAll(pageWithTenElementsAndSortedByAccountIdAscAndBalanceDesc);

        //Pagination DTO
        ResponseAccountsListDto dto = new ResponseAccountsListDto();
        int totalPages = accounts.getTotalPages();
        dto.setTotalPages(totalPages);

        if (page > totalPages )
            throw new TransactionError("The page you request not found, try page 1 or go to previous page");

        // url
        String url = httpServletRequest
                .getRequestURL().toString() + "?" + "page=";

        dto.setNextPage(totalPages == page ? null : url + String.valueOf(page + 1));
        dto.setPreviousPage(page == 1 ? null : url + String.valueOf(page - 1));
        dto.setAccountsDto(accountMapper.accountsToAccountsDto(accounts.getContent()));
        return dto;
    }
}
