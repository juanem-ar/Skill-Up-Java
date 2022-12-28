package com.alkemy.wallet.service;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import com.alkemy.wallet.exceptions.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import com.alkemy.wallet.dto.ResponseUsersDto;
import com.alkemy.wallet.model.User;

public interface IUserService extends UserDetailsService {
    Boolean existsByEmail(String email);
    public String deleteUser(Long id);
    public ResponseUsersDto findAllUsers(Integer page, HttpServletRequest httpServletRequest) throws Exception;
    Optional<User> findById(Long id);
	public User getUserById(Long userId);
    UserDetails loadUserByUsername(String email);
    ResponseDetailsUserDto getUserDetails(Long id, String token) throws Exception;
    ResponseDetailsUserDto updateUserDetails(Long id, PatchRequestUserDto dto, String token) throws Exception;
}
