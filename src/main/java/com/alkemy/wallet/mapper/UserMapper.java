package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.model.ERoles;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private IAccountRepository iAccountRepository;
    @Autowired
    private IRoleRepository iRoleRepository;

    public User toEntity(RequestUserDto dto) {
        User userEntity = new User();
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setEmail(dto.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userEntity.setRole(this.convertRole(dto.getRole()));
        return userEntity;
    }
    public ResponseUserDto toDto(User entity) {
        ResponseUserDto dto = new ResponseUserDto();
        dto.setFirstName(entity.getFirstName());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setAccounts(iAccountRepository.findAllByUserId(entity.getId()));
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setCreationDate(entity.getCreationDate());
        dto.setDeleted(entity.getDeleted());
        return dto;
    }

    public Role convertRole (String name){
        return name.equalsIgnoreCase("ADMIN") ?
                iRoleRepository.findByName(ERoles.ROLE_ADMIN) : iRoleRepository.findByName(ERoles.ROLE_USER);
    }
}
