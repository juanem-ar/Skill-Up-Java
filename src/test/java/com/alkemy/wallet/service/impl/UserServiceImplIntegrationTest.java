package com.alkemy.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IUserService;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
class UserServiceImplIntegrationTest {
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IUserRepository userRepository;

	@Test
	@Transactional
	void findAllUsers_DataBaseWithOneUser_ReturnAListWithOneDto() {
		User user1 = new User();
		
		String firstName = "first";
		user1.setFirstName(firstName);
		
		user1.setLastName("first");
		user1.setEmail("first@email.com");
		user1.setPassword("first password");
		userRepository.save(user1);
		
		List<ResponseUserDto> result = userService.findAllUsers();
		
		assertEquals(1, result.size());
		assertEquals(firstName, result.get(0).getFirstName());
	}

}
