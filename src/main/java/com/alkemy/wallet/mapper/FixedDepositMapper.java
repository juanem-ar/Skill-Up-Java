package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.FixedDepositDto;
import com.alkemy.wallet.model.FixedTermDeposit;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

@Component
public class FixedDepositMapper {
    public FixedTermDeposit toEntity(FixedDepositDto dto) {
        FixedTermDeposit entity = new FixedTermDeposit();
        entity.setAmount(dto.getAmount());
        entity.setInterest((dto.getPeriod() * 0.5)*dto.getAmount()/100);
        //TODO revisar persistencia de fecha de closingDate
        entity.setCreationDate(new Timestamp(System.currentTimeMillis()));
        entity.setClosingDate(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(dto.getPeriod(), ChronoUnit.DAYS)));
        return entity;
    }
}
