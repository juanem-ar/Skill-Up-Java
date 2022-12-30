package com.alkemy.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseDetailsUserDto;
import com.alkemy.wallet.dto.ResponseUsersDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.UserNotFoundException;
import com.alkemy.wallet.mapper.IuserMapper;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IAccountService;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
  /*
  @Mock
  private IUserRepository userRepository;

  @Mock
  private IuserMapper iUserMapper;

  @Mock
  private UserMapper userMapper;

  @Mock
  private IAccountService AccountServiceImpl;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private EntityManager entityManager;

  private Session session = Mockito.mock(Session.class);

  private Filter filter = Mockito.mock(Filter.class);

  @InjectMocks
  private UserServiceImpl userService;

  @BeforeEach
  public void createMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void findAllUsers_ListWithTwoUsers_ListWithTwoDtos() {
    List<User> users = new ArrayList<>();
    users.add(new User());
    users.add(new User());

    // entity manager
    when(entityManager.unwrap(Session.class)).thenReturn(session);
    when(session.enableFilter("deletedUserFilter"))
        .thenReturn(filter);
    when(filter.setParameter("isDeleted", false))
        .thenReturn(filter);

    when(userRepository.findAll()).thenReturn(users);

    List<ResponseDetailsUserDto> responseUserDtos = new ArrayList<>();
    responseUserDtos.add(new ResponseDetailsUserDto());
    responseUserDtos.add(new ResponseDetailsUserDto());

    when(iUserMapper.toResponseDetailsUserDtos(users))
        .thenReturn(responseUserDtos);

    ResponseUsersDto result = userService.findAllUsers(null, null);

    assertEquals(2, result.getUserDtos().size());
  }


  @Test
  void findAllUsers_PageIsEmpty_ThrowBadRequestException() {
    Integer page = 9;

    // entity manager
    when(entityManager.unwrap(Session.class)).thenReturn(session);
    when(session.enableFilter("deletedUserFilter"))
        .thenReturn(filter);
    when(filter.setParameter("isDeleted", false))
        .thenReturn(filter);

    when(userRepository.findAll(any(PageRequest.class)))
        .thenReturn(Page.empty());

    assertThrows(BadRequestException.class,
        () -> userService.findAllUsers(page, null));
  }


  @Test
  void getUserById_NotFound_ThrowException() {
    Long id = 2L;
    when(userRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.getUserById(id));
  }


  @Test
  void getUserById_UserExist_ReturnUser() {
    Long id = 2L;
    when(userRepository.findById(id))
        .thenReturn(Optional.of(new User()));

    User result = userService.getUserById(id);

    assertNotNull(result);
  }


  @Test
  void getUserDetails_IdAndTokenUserIdAreNotEqual_ThrowBadRequestException() {
    Long userId = 1L;
    Long tokenUserId = 2L;
    String token = "token";

    when(jwtUtils.getJwt(token)).thenReturn(token);
    when(jwtUtils.extractUserId(token)).thenReturn(tokenUserId);

    assertThrows(BadRequestException.class,
        () -> userService.getUserDetails(userId, token));
  }


  @Test
  void getUserDetails_IdAndTokenUserIdAreEqualAndNotFoundUser_ThrowUserNotFoundException() {
    Long userId = 1L;
    Long tokenUserId = 1L;
    String token = "token";

    when(jwtUtils.extractUserId(token)).thenReturn(tokenUserId);
    when(jwtUtils.getJwt(token)).thenReturn(token);
    
    when(userRepository.findById(tokenUserId))
        .thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.getUserDetails(userId, token));
  }


  @Test
  void getUserDetails_IdAndTokenUserIdAreEqualAndFoundUser_ReturnDto() {
    Long userId = 1L;
    Long tokenUserId = 1L;
    String token = "token";

    when(jwtUtils.getJwt(token)).thenReturn(token);
    when(jwtUtils.extractUserId(token)).thenReturn(tokenUserId);

    User user = new User();
    when(userRepository.findById(tokenUserId))
        .thenReturn(Optional.of(user));

    when(iUserMapper.toResponseDetailsUserDto(user))
        .thenReturn(new ResponseDetailsUserDto());

    ResponseDetailsUserDto result =
        userService.getUserDetails(userId, token);

    assertNotNull(result);
  }


  @Test
  void updateUserDetails_IdAndTokenUserIdAreNotEqual_ThrowBadRequestException() {
    Long userId = 1L;
    Long tokenUserId = 2L;
    String token = "token";
    PatchRequestUserDto dto = new PatchRequestUserDto();

    when(jwtUtils.getJwt(token)).thenReturn(token);
    when(jwtUtils.extractUserId(token)).thenReturn(tokenUserId);

    assertThrows(BadRequestException.class,
        () -> userService.updateUserDetails(userId, dto, token));
  }


  @Test
  void updateUserDetails_IdAndTokenUserIdAreEqualNotFounUser_ThrowUserNotFoundException() {
    Long userId = 1L;
    Long tokenUserId = 1L;
    String token = "token";
    PatchRequestUserDto dto = new PatchRequestUserDto();

    when(jwtUtils.getJwt(token)).thenReturn(token);
    when(jwtUtils.extractUserId(token)).thenReturn(tokenUserId);

    when(userRepository.findById(tokenUserId))
        .thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.updateUserDetails(userId, dto, token));
  }


  @Test
  void updateUserDetails_IdAndTokenUserIdAreEqualAndFoundUser_ReturnDto() {
    Long userId = 1L;
    Long tokenUserId = 1L;
    String token = "token";
    PatchRequestUserDto patchDto = new PatchRequestUserDto();

    when(jwtUtils.getJwt(token)).thenReturn(token);
    when(jwtUtils.extractUserId(token)).thenReturn(tokenUserId);

    User user = new User();

    when(userRepository.findById(tokenUserId))
        .thenReturn(Optional.of(user));

    when(userRepository.save(user)).thenReturn(user);

    when(iUserMapper.updateUser(patchDto, user)).thenReturn(user);

    when(iUserMapper.toResponseDetailsUserDto(user))
        .thenReturn(new ResponseDetailsUserDto());

    ResponseDetailsUserDto result =
        userService.updateUserDetails(userId, patchDto, token);

    assertNotNull(result);
  }
*/
}
