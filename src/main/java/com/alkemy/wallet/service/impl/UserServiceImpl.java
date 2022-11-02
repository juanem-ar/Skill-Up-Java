package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements IUserService {
    private IUserRepository  iUserRepository;

    @Autowired
    public UserServiceImpl(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public String deleteUser(Long id) {
        iUserRepository.deleteById(id);
        return "delete user with number" + id ;
    }
}
