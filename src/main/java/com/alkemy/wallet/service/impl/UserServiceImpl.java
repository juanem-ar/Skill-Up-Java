package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.mapper.IuserMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IUserService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    private IUserRepository  iUserRepository;
    
    @Autowired
	private IuserMapper userMapper;

    @Autowired
    public UserServiceImpl(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public String deleteUser(Long id) {
        iUserRepository.deleteById(id);
        return "delete user with number" + id ;
    }

	@Override
	public List<ResponseUserDto> findAllUsers() {
		List<User> users = iUserRepository.findAll();
		
		List<ResponseUserDto> dtos = new ArrayList<>();
		
		users.forEach(user -> {
			dtos.add(userMapper.modelToResponseUserDto(user));
			});
		
		return dtos;
	}
}
