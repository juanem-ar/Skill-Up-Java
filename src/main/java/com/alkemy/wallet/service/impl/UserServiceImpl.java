package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.exceptions.UserNotFoundException;
import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.UserNotFoundUserException;
import com.alkemy.wallet.mapper.IuserMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.security.service.UserDetailsImpl;
import com.alkemy.wallet.service.IUserService;
import lombok.AllArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.alkemy.wallet.dto.ResponseUsersDto;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Objects;
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
    public String deleteUser(Long id) {
       User userSelected = iUserRepository.findById(id).orElseThrow(()-> new UserNotFoundUserException("Not found User with number id: "+ id));
       userSelected.setDeleted(true);
       iUserRepository.save(userSelected);
       return "delete user with number" + id ;
    }

	@Override
	public ResponseUsersDto findAllUsers(
		Integer page, 
		HttpServletRequest httpServletRequest) {
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
        if(iUserRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }

	@Override
	public ResponseDetailsUserDto getUserDetails(Long id, String token) {
		Long tokenUserId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
		
		sameIdOrThrowException(id, tokenUserId);
		
		return iUserMapper.toResponseDetailsUserDto(getUserById(tokenUserId));
	}

	@Override
	public ResponseDetailsUserDto updateUserDetails(
		Long id,
		PatchRequestUserDto dto,
		String token) {
		Long tUserId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
		
		sameIdOrThrowException(id, tUserId);
		
		User user = getUserById(tUserId);
		
		user = iUserRepository.save(
			iUserMapper.updateUser(dto, user));
		
		return iUserMapper.toResponseDetailsUserDto(user);
	}
	
	
	private void sameIdOrThrowException(Long userId, Long tokenUserId) {
		if(!Objects.equals(userId, tokenUserId))
			throw new BadRequestException("not same");
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
