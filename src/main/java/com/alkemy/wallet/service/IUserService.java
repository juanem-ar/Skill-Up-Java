package com.alkemy.wallet.service;

import com.alkemy.wallet.model.User;

import java.util.Optional;

public interface IUserService {
    public String deleteUser(Long id);

    Optional<User> findById(Long id);
}
