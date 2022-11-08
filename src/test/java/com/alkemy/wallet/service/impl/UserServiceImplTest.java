package com.alkemy.wallet.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.UserNotFoundException;
import com.alkemy.wallet.mapper.IuserMapper;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IAccountService;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
	@Mock
    private IUserRepository userRepository;
	@Mock
    private IuserMapper iUserMapper;
	@Mock
	private UserMapper userMapper;
	@Mock
    private IAccountService AccountServiceImpl;
	@Mock
    private AuthenticationManager authenticationManager;
	@Mock
    private JwtUtils jwtUtils;
	
	@InjectMocks
	private UserServiceImpl userService;


	@Test
	void findAllUsers_ListWithTwoUsers_ListWithTwoDtos() {
		List<User> users = new ArrayList<>();
		users.add(new User());
		users.add(new User());

		when(userRepository.findAll()).thenReturn(users);
		
		List<ResponseUserDto> responseUserDtos = new ArrayList<>();
		responseUserDtos.add(new ResponseUserDto());
		responseUserDtos.add(new ResponseUserDto());
		
		when(iUserMapper.usersToResponseUserDtos(users))
			.thenReturn(responseUserDtos);

		List<ResponseUserDto> result = userService.findAllUsers();

		assertEquals(2, result.size());
	}


	@Test
	void getUserById_NotFound_ThrowException() {
		Long id = 2L;
		when(userRepository.findById(id)).thenReturn(Optional.empty());
		
		assertThrows(UserNotFoundException.class, ()-> userService.getUserById(id));
	}


	@Test
	void getUserById_UserExist_ReturnUser() {
		Long id = 2L;
		when(userRepository.findById(id)).thenReturn(Optional.of(new User()));
		
		User result = userService.getUserById(id);
		
		assertNotNull(result);
	}
	
	@Test
	void getUserDetails_IdAndTokenUserIdAreNotEqual_ThrowBadRequestException() {
		Long userId = 1L;
		Long tokenUserId = 2L;
		String token = "token";
		
		when(jwtUtils.extractUserId(token)).thenReturn(tokenUserId);
		
		assertThrows(
			BadRequestException.class,
			() -> userService.getUserDetails(userId, token));
	}
	
	@Test
	void getUserDetails_IdAndTokenUserIdAreEqualAndNotFoundUser_ThrowUserNotFoundException() {
		Long userId = 1L;
		Long tokenUserId = 1L;
		String token = "token";
		
		when(jwtUtils.extractUserId(token)).thenReturn(tokenUserId);
		
		when(userRepository.findById(tokenUserId))
			.thenReturn(Optional.empty());

		assertThrows(
			UserNotFoundException.class,
			() -> userService.getUserDetails(userId, token));
	}
	
	@Test
	void getUserDetails_IdAndTokenUserIdAreEqualAndFoundUser_ReturnDto() {
		Long userId = 1L;
		Long tokenUserId = 1L;
		String token = "token";
		
		when(jwtUtils.extractUserId(token)).thenReturn(tokenUserId);
		
		User user = new User();
		when(userRepository.findById(tokenUserId))
			.thenReturn(Optional.of(user));
		
		when(iUserMapper.toResponseUserDto(user))
			.thenReturn(new ResponseUserDto());
		
		ResponseUserDto result = userService.getUserDetails(userId, token);
		
		assertNotNull(result);
	}

}
