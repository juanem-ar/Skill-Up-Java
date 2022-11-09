package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.exceptions.UserNotFoundException;
import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.mapper.IuserMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.dto.ResponseUserDto;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private IUserRepository  iUserRepository;
    private JwtUtils jwtUtils;
    private IuserMapper iUserMapper;
    @Autowired
    public UserServiceImpl( IuserMapper iUserMapper, IUserRepository iUserRepository, JwtUtils jwtUtils) {
        this.iUserRepository = iUserRepository;
        this.jwtUtils = jwtUtils;
        this.iUserMapper = iUserMapper;
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
		return iUserMapper.usersToResponseUserDtos(iUserRepository.findAll());
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
	@Override
    public Boolean existsByEmail(@PathVariable String email){
        if(iUserRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }

	@Override
	public ResponseUserDto getUserDetails(Long id, String token) {
		Long tokenUserId = jwtUtils.extractUserId(token);
		
		sameIdOrThrowException(id, tokenUserId);
		
		return iUserMapper.toResponseUserDto(getUserById(tokenUserId));
	}

	@Override
	public ResponseUserDto updateUserDetails(
		Long id,
		PatchRequestUserDto dto,
		String token) {
		Long tUserId = jwtUtils.extractUserId(token);
		
		sameIdOrThrowException(id, tUserId);
		
		User user = getUserById(tUserId);
		
		user = iUserRepository.save(
			iUserMapper.updateUser(dto, user));
		
		return iUserMapper.toResponseUserDto(user);
	}
	
	
	private void sameIdOrThrowException(Long userId, Long tokenUserId) {
		if(!Objects.equals(userId, tokenUserId))
			throw new BadRequestException();
	}

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return iUserRepository.findByEmail(email);
    }
}
