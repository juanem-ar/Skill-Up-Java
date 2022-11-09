package com.alkemy.wallet.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.dto.ResponseUsersDto;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;

public interface IUserService {
    Boolean existsByEmail(String email);
    ResponseUserDto saveUser(ResponseUserDto dto) throws Exception;
    public String deleteUser(Long id);
    public ResponseUsersDto findAllUsers(
    	Integer page, 
    	HttpServletRequest httpServletRequest);
    Optional<User> findById(Long id);
	public User getUserById(Long userId);
    AuthenticationResponseDto login(AuthenticationRequestDto dto) throws Exception;
    ResponseUserDto getUserDetails(Long id, String token);
	ResponseUserDto updateUserDetails(
		Long id,
		PatchRequestUserDto dto,
		String token);
}
