package com.alkemy.wallet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import com.alkemy.wallet.dto.ResponseFixedDepositDto;
import com.alkemy.wallet.model.FixedTermDeposit;
import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IFixedTermDepositMapper {
  ResponseFixedDepositDto toResponseFixedDepositDto(FixedTermDeposit deposit);
  List<ResponseFixedDepositDto> fixedDepositListToResponseList(List<FixedTermDeposit> list);
}
