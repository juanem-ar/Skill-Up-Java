package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.service.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolServiceImpl implements IRolService {
    @Autowired
    private IRoleRepository iRoleRepository;

    @Override
    public Role getById(Long id) throws ResourceNotFoundException {
        return iRoleRepository.getReferenceById(id);
    }

    @Override
    public List<Role> getAll() {
        return iRoleRepository.findAll();
    }
}
