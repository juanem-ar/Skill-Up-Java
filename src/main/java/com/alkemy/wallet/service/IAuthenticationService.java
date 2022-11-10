package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;

public interface IAuthenticationService {
    ResponseUserDto saveUser(ResponseUserDto dto) throws Exception;
    AuthenticationResponseDto login(AuthenticationRequestDto dto) throws Exception;
}
