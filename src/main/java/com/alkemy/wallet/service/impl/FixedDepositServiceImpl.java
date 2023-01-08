package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.FixedDepositDto;
import com.alkemy.wallet.dto.ResponseSimulatedFixedDepositDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.mapper.FixedDepositMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IFixedTermDepositRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IFixedDepositService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class FixedDepositServiceImpl implements IFixedDepositService {
    public static final int MIN_DAYS = 30;
    private final IAccountRepository iAccountRepository;
    private final IUserRepository userRepository;
    private final IFixedTermDepositRepository iFixedTermDepositRepository;
    private final FixedDepositMapper fixedDepositMapper;
   
    public String addFixedDeposit(Authentication authentication,Double amount, String currency, int period) throws Exception{

        currencyArsOrUsdOrThrowError(currency);

        Long userId = userRepository.findByEmail(authentication.getName()).getId();
        FixedDepositDto dto = new FixedDepositDto(amount, ECurrency.valueOf(currency.toUpperCase()),period);

        Account account = iAccountRepository.getReferenceByUserIdAndCurrency(userId,dto.getCurrency());

        if(account.getBalance() < dto.getAmount())
            throw new BadRequestException("You don't create a fixed deposit with a lower balance.");

        FixedTermDeposit fixedTermDeposit = fixedDepositMapper.toEntity(dto);

        long compareDate = ChronoUnit.DAYS.between(Timestamp.from(fixedTermDeposit.getCreationDate().toInstant()).toInstant(),fixedTermDeposit.getClosingDate().toInstant());

        minimumTermOrThrowError((int) compareDate);

        account.setBalance(account.getBalance() - dto.getAmount());
        iAccountRepository.save(account);
        fixedTermDeposit.setAccount(account);
        iFixedTermDepositRepository.save(fixedTermDeposit);
        return HttpStatus.CREATED.getReasonPhrase().concat(" fixed deposit (ID: " + fixedTermDeposit.getId() + ")");
    }

    @Override
    public ResponseSimulatedFixedDepositDto simulateFixedDeposit(String currency, int period, Double amount) throws Exception {
        currencyArsOrUsdOrThrowError(currency);
        if (amount <= 0) { throw new BadRequestException("Invalid amount."); }
        minimumTermOrThrowError(period);
        FixedDepositDto dto = new FixedDepositDto(amount,ECurrency.valueOf(currency.toUpperCase()),period);
        return fixedDepositMapper.toSimulateFixedDeposit(dto);
    }
    public void currencyArsOrUsdOrThrowError(String currency) throws BadRequestException {
        if (!currency.equalsIgnoreCase("ARS") && !currency.equalsIgnoreCase("USD"))
            throw new BadRequestException("Currency value is not found. Insert ARS or USD");
    }
    public void minimumTermOrThrowError(int period) throws BadRequestException{
        if(period < MIN_DAYS )
            throw new BadRequestException("The minimum term is 30 days.");
    }

    @Override
    public List<FixedTermDeposit> findAllBy(Account account) {
      return iFixedTermDepositRepository.findByAccount(account);
    }
}
