package com.alkemy.wallet.service;

import java.util.List;
import java.util.Optional;

import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;

public interface IUserService {
    Boolean existsByEmail(String email);
    ResponseUserDto saveUser(ResponseUserDto dto) throws Exception;
    public String deleteUser(Long id);
    public List<ResponseUserDto> findAllUsers();
    Optional<User> findById(Long id);
	public User getUserById(Long userId);
    AuthenticationResponseDto login(AuthenticationRequestDto dto) throws Exception;
    ResponseUserDto getUserDetails(Long id, String token);
	ResponseUserDto updateUserDetails(
		Long id,
		PatchRequestUserDto dto,
		String token);
}
