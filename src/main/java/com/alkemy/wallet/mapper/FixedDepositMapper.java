package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.FixedDepositDto;
import com.alkemy.wallet.dto.ResponseSimulateFixedDepositDto;
import com.alkemy.wallet.model.FixedTermDeposit;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

@Component
public class FixedDepositMapper {
    public FixedTermDeposit toEntity(FixedDepositDto dto) {
        FixedTermDeposit entity = new FixedTermDeposit();
        Double interest = (dto.getPeriod() * 0.005)*dto.getAmount();
        entity.setAmount(dto.getAmount());
        entity.setInterest(interest);
        entity.setCreationDate(new Timestamp(System.currentTimeMillis()));
        entity.setClosingDate(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(dto.getPeriod(), ChronoUnit.DAYS)));
        return entity;
    }
    public ResponseSimulateFixedDepositDto toSimulateFixedDeposit(FixedDepositDto dto){
        Double amount = dto.getAmount();
        Double interest = (dto.getPeriod() * 0.005)*dto.getAmount();
        ResponseSimulateFixedDepositDto simulated = new ResponseSimulateFixedDepositDto();
        simulated.setAmount(amount);
        simulated.setCurrency(dto.getCurrency());
        simulated.setPeriod(dto.getPeriod());
        simulated.setCreationLocalDate(new Timestamp(System.currentTimeMillis()).toLocalDateTime().toLocalDate());
        simulated.setClosingLocalDate(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(dto.getPeriod(), ChronoUnit.DAYS)).toLocalDateTime().toLocalDate());
        simulated.setInterest(shortDouble(interest));
        simulated.setTotalAmount(shortDouble(amount + interest));
        return simulated;
    }
    public Double shortDouble(Double number){
        return Double.parseDouble(String.valueOf(new BigDecimal(number).setScale(2, RoundingMode.HALF_UP)));
    }
}
