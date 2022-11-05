package com.alkemy.wallet.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.exceptions.UserNotFoundException;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.mapper.IuserMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IUserService;




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
       User userSelected = iUserRepository.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found User with number id: "+ id));
       userSelected.setDeleted(true);
       iUserRepository.save(userSelected);
       return "delete user with number" + id ;
    }

	@Override
	public List<ResponseUserDto> findAllUsers() {
		return userMapper.usersToResponseUserDtos(iUserRepository.findAll());
	}

    @Override
    public Optional<User> findById(Long id) {
        return iUserRepository.findById(id);
    }

	@Override
	public User getUserById(Long userId) {
		Optional<User> userOptional = iUserRepository.findById(userId);
		
		if(userOptional.isEmpty())
			throw new UserNotFoundException();
		
		return userOptional.get();
	}
}
