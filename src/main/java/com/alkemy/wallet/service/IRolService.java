package com.alkemy.wallet.service;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.service.generic.GenericServiceAPI;

import java.util.List;

public interface IRolService extends GenericServiceAPI<Role, Long> {

    Role getById(Long id) throws ResourceNotFoundException;
    List<Role> getAll();
}
