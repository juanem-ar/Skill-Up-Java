package com.alkemy.wallet.security.service;

import com.alkemy.wallet.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsCustomService implements UserDetailsService {
    @Autowired
    private IUserRepository iUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.alkemy.wallet.model.User userEntity = iUserRepository.findByEmail(email);
        if (userEntity == null){
            throw new UsernameNotFoundException(("Username or password not found"));
        }
        return new User(userEntity.getEmail(), userEntity.getPassword(), Collections.emptyList());
    }
}



