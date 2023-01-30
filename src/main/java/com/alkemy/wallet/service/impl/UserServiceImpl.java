package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.exceptions.*;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import com.alkemy.wallet.mapper.IuserMapper;
import com.alkemy.wallet.model.ERoles;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.UserDetailsImpl;
import com.alkemy.wallet.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.alkemy.wallet.dto.ResponseUserListDto;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository  iUserRepository;
    private final IuserMapper iUserMapper;
    public static final Integer USERS_FOR_PAGE = 10;

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
	public ResponseUserListDto findAllUsers(Integer page, HttpServletRequest httpServletRequest) throws Exception {
		if (page <= 0)
			throw new TransactionError("You request page not found, try page 1");

		Pageable pageWithTenElementsAndSortedByIdAscAndRoleDesc = PageRequest.of(page-1,USERS_FOR_PAGE,
				Sort.by("id")
						.ascending()
						.and(Sort.by("role")
								.descending()));
		Page<User> userList = iUserRepository.findAll(pageWithTenElementsAndSortedByIdAscAndRoleDesc);

		//Pagination DTO
		ResponseUserListDto dto = new ResponseUserListDto();
		int totalPages = userList.getTotalPages();
		dto.setTotalPages(totalPages);

		if (page > totalPages )
			throw new TransactionError("The page you request not found, try page 1 or go to previous page");

		// url
		String url = httpServletRequest
				.getRequestURL().toString() + "?" + "page=";

		dto.setNextPage(totalPages == page ? null : url + String.valueOf(page + 1));
		dto.setPreviousPage(page == 1 ? null : url + String.valueOf(page - 1));
		dto.setUsersDto(iUserMapper.toResponseDetailsUserDtos(userList.getContent()));
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
		iUserRepository.save(iUserMapper.updateUser(dto, entity));
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
