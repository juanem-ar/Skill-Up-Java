package com.alkemy.wallet.mapper;

import org.mapstruct.Mapper;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.User;

@Mapper(componentModel = "spring")
public interface IuserMapper {

	ResponseUserDto modelToResponseUserDto(User user);

}
