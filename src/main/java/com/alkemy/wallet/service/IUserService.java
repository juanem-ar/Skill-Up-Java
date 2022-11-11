package com.alkemy.wallet.service;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.dto.ResponseUsersDto;
import com.alkemy.wallet.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    Boolean existsByEmail(String email);
    public String deleteUser(Long id);
    public ResponseUsersDto findAllUsers(
    	Integer page, 
    	HttpServletRequest httpServletRequest);
    Optional<User> findById(Long id);
	public User getUserById(Long userId);
    User loadUserByUsername(String email);
    ResponseUserDto getUserDetails(Long id, String token);
	ResponseUserDto updateUserDetails(
		Long id,
		PatchRequestUserDto dto,
		String token);
}
