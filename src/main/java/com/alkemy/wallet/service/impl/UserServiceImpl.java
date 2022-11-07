package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.exceptions.UserNotFoundException;
import com.alkemy.wallet.dto.CurrencyDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    //private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl( IUserRepository iUserRepository /*, BCryptPasswordEncoder passwordEncoder*/ , @Lazy IAccountService iAccountServiceImpl,
                            AuthenticationManager authenticationManager, UserMapper userMapper, JwtUtils jwtUtils) {
        this.iUserRepository = iUserRepository;
        this.userMapper = userMapper;
        this.iAccountServiceImpl = iAccountServiceImpl;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        /*this.passwordEncoder = passwordEncoder;*/
    }

    @Override
    public ResponseUserDto saveUser(ResponseUserDto dto) throws Exception{
        if (!existsByEmail(dto.getEmail())) {
            try{
                User entity = userMapper.toEntity(dto);
                User entitySaved = iUserRepository.save(entity);
                this.iAccountServiceImpl.addAccount(entitySaved.getEmail(), new CurrencyDto(ECurrency.ARS));
                this.iAccountServiceImpl.addAccount(entitySaved.getEmail(), new CurrencyDto(ECurrency.USD));
                ResponseUserDto responseDto = userMapper.toDto(entitySaved, entitySaved.getId());
                AuthenticationResponseDto login = login(userMapper.toRequestDto(responseDto));
                responseDto.setJwt(login.getJwt());
                return responseDto;
            }catch (Exception e){
                throw new BadRequestException("Incorrect format email. Input email is " + e.getCause());
            }
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
	public User getUserById(Long userId) {
		Optional<User> userOptional = iUserRepository.findById(userId);
		
		if(userOptional.isEmpty())
			throw new UserNotFoundException();
		
		return userOptional.get();
	}
    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto authRequest) throws Exception {
        if (existsByEmail(authRequest.getEmail())) {
            if(authRequest.getPassword().equals(iUserRepository.findByEmail(authRequest.getEmail()).getPassword())){
            //if(passwordEncoder.matches(authRequest.getPassword(), iUserRepository.findByEmail(authRequest.getEmail()).getPassword())){
                UserDetails userDetails;
                try{
                    Authentication auth = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
                    );
                    userDetails = (UserDetails) auth.getPrincipal();
                } catch (BadCredentialsException e){
                    throw new Exception("Incorrect username or password", e);
                }
                final String username = authRequest.getEmail();
                User dto = iUserRepository.findByEmail(username);
                final String jwt = jwtUtils.generateToken(userDetails);
                return new AuthenticationResponseDto(username,jwt);
            }else{
                throw new BadRequestException("Incorrect password:");
            }
        } else{
            throw new BadRequestException("There isn't an account with that email adress or introduced an incorrect password: " + authRequest.getEmail());
        }
    }
    @Override
    public Boolean existsByEmail(@PathVariable String email){
        if(iUserRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }

}
