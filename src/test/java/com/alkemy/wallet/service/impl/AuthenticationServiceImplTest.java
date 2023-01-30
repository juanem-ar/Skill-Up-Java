package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.config.PasswordEncoder;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Date;
import java.sql.Timestamp;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {
    @Mock
    private IUserRepository iUserRepository;
    @Mock
    private IAccountService iAccountService;
    @Mock
    private IUserService iUserService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private RequestUserDto reqUser;
    private AuthenticationRequestDto authReq;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reqUser = RequestUserDto.builder()
                .firstName("Juan")
                .lastName("Lopez")
                .email("test2@test.com")
                .password("12345678")
                .role("user")
                .build();

        AuthenticationRequestDto authReq = AuthenticationRequestDto.builder()
                .email("test2@test.com")
                .password("12345678")
                .build();
    }

    @Test
    void saveUserWithExistingEmailShouldThrowException(){
        when(iUserRepository.existsByEmail(reqUser.getEmail())).thenReturn(true);
        assertThrows(BadRequestException.class,()->{
            authenticationService.saveUser(reqUser);
        });
    }

    @Test
    void saveUserWithPasswordEmptyShouldThrowException(){
        reqUser.setPassword("");
        assertThrows(BadRequestException.class,()->{
            authenticationService.saveUser(reqUser);
        });
    }

}