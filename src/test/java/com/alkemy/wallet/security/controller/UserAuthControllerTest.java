package com.alkemy.wallet.security.controller;

import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;
import com.alkemy.wallet.service.IAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserAuthControllerTest {

    @Mock
    private IAuthenticationService iAuthenticationService;

    @InjectMocks
    private UserAuthController userAuthController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp() throws Exception {

        ResponseUserDto responseUserDto = ResponseUserDto.builder()
                .id(1L)
                .firstName("test")
                .lastName("LastTest")
                .email("test@hotmail.com")
                .password("12345678")
                .role(new Role())
                .jwt("token")
                .accounts(Arrays.asList(new Account()))
                .creationDate(new Timestamp(new Date().getTime()))
                .updateDate(new Timestamp(new Date().getTime()))
                .deleted(false)
                .build();

        when(iAuthenticationService.saveUser(any(RequestUserDto.class))).thenReturn(responseUserDto);

        assertNotNull(userAuthController.signUp(new RequestUserDto()));
    }

    @Test
    void signIn() throws Exception {
        AuthenticationResponseDto res = AuthenticationResponseDto.builder()
                .user("test@test.com")
                .jwt("token")
                .build();

        when(iAuthenticationService.login(any(AuthenticationRequestDto.class))).thenReturn(res);

        assertNotNull(userAuthController.signIn(new AuthenticationRequestDto()));
    }
}