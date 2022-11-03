package com.alkemy.wallet.service;

import java.util.List;
import java.util.Optional;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.User;

public interface IUserService {
    public String deleteUser(Long id);

    public List<ResponseUserDto> findAllUsers();

    Optional<User> findById(Long id);
}
