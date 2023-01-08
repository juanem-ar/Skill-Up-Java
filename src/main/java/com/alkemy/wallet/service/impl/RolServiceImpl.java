package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.service.IRolService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class RolServiceImpl implements IRolService {
    private final IRoleRepository iRoleRepository;

    @Override
    public Role getById(Long id) throws ResourceNotFoundException {
        return iRoleRepository.getReferenceById(id);
    }

    @Override
    public List<Role> getAll() {
        return iRoleRepository.findAll();
    }
}
