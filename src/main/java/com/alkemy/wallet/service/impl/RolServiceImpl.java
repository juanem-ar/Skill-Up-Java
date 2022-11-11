package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.mapper.RoleMapper;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.service.IRolService;
import com.alkemy.wallet.service.generic.GenericServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolServiceImpl extends GenericServiceImpl<Role, Long> implements IRolService {

    private final IRoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Override
    public Role getById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<Role> getAll() {
        return null;
    }

    @Override
    public JpaRepository<Role, Long> getRepository() {
        return roleRepository;
    }
}
