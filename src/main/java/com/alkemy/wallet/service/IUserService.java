package com.alkemy.wallet.service;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import com.alkemy.wallet.dto.PatchRequestUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import com.alkemy.wallet.dto.ResponseUserListDto;
import com.alkemy.wallet.model.User;

public interface IUserService extends UserDetailsService {
    Boolean existsByEmail(String email);
    String deleteUser(Long id, Authentication authentication) throws Exception;
    ResponseUserListDto findAllUsers(Integer page, HttpServletRequest httpServletRequest) throws Exception;
    Optional<User> findById(Long id);
	User getUserById(Long userId);
    UserDetails loadUserByUsername(String email);
    ResponseDetailsUserDto getUserDetail(Authentication authentication) throws Exception;
    ResponseDetailsUserDto getUserDetailById(Long id) throws Exception;
    ResponseDetailsUserDto updateUserDetails(Long id, PatchRequestUserDto dto, Authentication authentication) throws Exception;
}
