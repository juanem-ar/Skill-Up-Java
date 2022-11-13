package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;
import org.apache.coyote.Request;

public interface IAuthenticationService {
    ResponseUserDto saveUser(RequestUserDto dto) throws Exception;
    AuthenticationResponseDto login(AuthenticationRequestDto dto) throws Exception;
}
