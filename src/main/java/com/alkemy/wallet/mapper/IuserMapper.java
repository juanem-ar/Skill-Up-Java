package com.alkemy.wallet.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.User;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IuserMapper {

	List<ResponseUserDto> usersToResponseUserDtos(List<User> users);

	ResponseUserDto toResponseUserDto(User user);

	User updateUser(
		PatchRequestUserDto dto,
		@MappingTarget User target);

	ResponseDetailsUserDto toResponseDetailsUserDto(User user);
	List<ResponseDetailsUserDto> toResponseDetailsUserDtos(List<User> users);
}
