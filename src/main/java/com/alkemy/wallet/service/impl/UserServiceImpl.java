package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    private IUserRepository  iUserRepository;

    @Autowired
    public UserServiceImpl(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public String deleteUser(Long id) {
       User userSelected = iUserRepository.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found User with number id: "+ id));
       userSelected.setDeleted(true);
       iUserRepository.save(userSelected);
       return "delete user with number" + id ;
    }
}
