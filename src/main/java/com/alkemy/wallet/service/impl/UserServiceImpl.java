package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.exceptions.UserNotFoundException;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.mapper.IuserMapper;
import com.alkemy.wallet.model.ERoles;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.security.service.UserDetailsImpl;
import com.alkemy.wallet.service.IUserService;
import lombok.AllArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.alkemy.wallet.dto.ResponseUsersDto;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private IUserRepository  iUserRepository;
    private JwtUtils jwtUtils;
    private IuserMapper iUserMapper;
    private EntityManager entityManager;
    private static final Integer USERS_FOR_PAGE = 10;

    @Override
    public String deleteUser(Long id, Authentication authentication) throws Exception{
       User userSelected = iUserRepository.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found User with number id: "+ id));
	   User userAuth = iUserRepository.findByEmail(authentication.getName());
	   if (!userSelected.getEmail().equals(userAuth.getEmail()) && userAuth.getRole().getName().equals(ERoles.ROLE_USER))
			throw new ResourceNotFoundException("You don't have permission to delete this user");
	   userSelected.setDeleted(true);
       iUserRepository.save(userSelected);
       return "User id: " + id +", deleted.";
    }

	@Override
	public ResponseUsersDto findAllUsers(Integer page, HttpServletRequest httpServletRequest) throws Exception {
		ResponseUsersDto dto = new ResponseUsersDto();
		// activate hibernate filter
		Session session = entityManager.unwrap(Session.class);
		Filter filter = session.enableFilter("deletedUserFilter");
		filter.setParameter("isDeleted", false);
		
		// without request parameter
		if(page == null) {
			dto.setUserDtos(
				iUserMapper.toResponseDetailsUserDtos(
					iUserRepository.findAll()));
			return dto;
		}
		
		// with request parameter
		Page<User> users = iUserRepository.findAll(
			PageRequest.of(page, USERS_FOR_PAGE));
		
		if(users.isEmpty())
			throw new BadRequestException("Insert an user");
		
		dto.setUserDtos(
			iUserMapper.toResponseDetailsUserDtos(
				users.toList()));
		
		// url
		String url = httpServletRequest
			.getRequestURL().toString() + "?" + "page=";
		
		if(users.hasPrevious()) {
			int previousPage = users.getNumber() - 1;
			dto.setPreviousPage(url + previousPage);
		}
		
		if (users.hasNext()) {
			int nextPage = users.getNumber() + 1;
			dto.setPreviousPage(url + nextPage);
		}
		
		return dto;
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
        return iUserRepository.existsByEmail(email);
    }

	@Override
	public ResponseDetailsUserDto getUserDetail(Authentication authentication) throws Exception {
		User entity = iUserRepository.findByEmail(authentication.getName());
		return iUserMapper.toResponseDetailsUserDto(entity);
	}

	@Override
	public ResponseDetailsUserDto getUserDetailById(Long id) throws Exception {
		User entity = iUserRepository.findById(id).orElseThrow(()->
				new UserNotFoundUserException("Not found Account with number id: "+ id));
		return iUserMapper.toResponseDetailsUserDto(entity);
	}

	@Override
	public ResponseDetailsUserDto updateUserDetails(Long id, PatchRequestUserDto dto, Authentication authentication) throws Exception {
		User entity = iUserRepository.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found Account with number id: "+ id));
		if (!entity.getEmail().equals(authentication.getName()))
			throw new ResourceNotFoundException("You don't have permission to edit this user");
		try{
		iUserRepository.save(iUserMapper.updateUser(dto, entity));
		}catch (Exception ex){
			throw new BadRequestException(ex.getMessage() + ". Please check the data inserted");
		}
		return iUserMapper.toResponseDetailsUserDto(entity);
	}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User userEntity = iUserRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException("username or password not found");
		}
        return UserDetailsImpl.build(userEntity);
    }
}
