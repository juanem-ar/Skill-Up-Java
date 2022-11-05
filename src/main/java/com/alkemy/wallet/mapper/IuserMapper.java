package com.alkemy.wallet.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.User;

@Mapper(componentModel = "spring")
public interface IuserMapper {

	List<ResponseUserDto>  usersToResponseUserDtos(List<User> users);

}
