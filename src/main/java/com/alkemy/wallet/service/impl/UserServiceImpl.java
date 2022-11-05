package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.CurrencyDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.mapper.IuserMapper;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;




@Service
public class UserServiceImpl implements IUserService {
    private IUserRepository  iUserRepository;
    private IuserMapper  iUserMapper;
	private UserMapper userMapper;
    private IAccountService iAccountServiceImpl;

    @Autowired
    public UserServiceImpl(IUserRepository iUserRepository, UserMapper userMapper, IuserMapper  iUserMapper,@Lazy IAccountService iAccountServiceImpl ) {
        this.iUserRepository = iUserRepository;
        this.userMapper = userMapper;
        this.iUserMapper = iUserMapper;
        this.iAccountServiceImpl = iAccountServiceImpl;
    }

    @Override
    public ResponseUserDto saveUser(ResponseUserDto dto) throws Exception {
        if (!existsByEmail(dto.getEmail())) {
            User entity = userMapper.toEntity(dto);
            User entitySaved = iUserRepository.save(entity);
            this.iAccountServiceImpl.addAccount(entitySaved.getEmail(), new CurrencyDto(ECurrency.ARS));
            this.iAccountServiceImpl.addAccount(entitySaved.getEmail(), new CurrencyDto(ECurrency.USD));
            ResponseUserDto userdto = userMapper.toDto(entitySaved, entitySaved.getId());
            return userdto;
        } else {
            throw new BadRequestException("There is an account with that email adress: " + dto.getEmail());
        }
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
    public Boolean existsByEmail(@PathVariable String email){
        if(iUserRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }
}
