package com.alkemy.wallet.service;

import java.util.List;
import java.util.Optional;
import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    Boolean existsByEmail(String email);
    public String deleteUser(Long id);
    public List<ResponseUserDto> findAllUsers();
    Optional<User> findById(Long id);
	public User getUserById(Long userId);
    User loadUserByUsername(String email);
    ResponseUserDto getUserDetails(Long id, String token);
	ResponseUserDto updateUserDetails(
		Long id,
		PatchRequestUserDto dto,
		String token);
}
