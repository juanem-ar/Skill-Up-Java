package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private IAccountRepository iAccountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User toEntity(ResponseUserDto dto) {
        User userEntity = new User();
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setEmail(dto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userEntity.setRole(dto.getRole());
        userEntity.setUpdateDate(dto.getUpdateDate());
        userEntity.setCreationDate(dto.getCreationDate());
        userEntity.setDeleted(dto.getDeleted());
        return userEntity;
    }

    public ResponseUserDto toDto(User entity, Long idUser) {
        ResponseUserDto dto = new ResponseUserDto();
        dto.setFirstName(entity.getFirstName());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setAccounts(iAccountRepository.findAllByUserId(idUser));
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setCreationDate(entity.getCreationDate());
        dto.setDeleted(entity.getDeleted());
        return dto;
    }
}
