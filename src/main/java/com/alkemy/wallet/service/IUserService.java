package com.alkemy.wallet.service;

import java.util.List;

import com.alkemy.wallet.dto.ResponseUserDto;

public interface IUserService {
    public String deleteUser(Long id);
    
    public List<ResponseUserDto> findAllUsers();
}
