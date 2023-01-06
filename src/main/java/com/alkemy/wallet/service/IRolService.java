package com.alkemy.wallet.service;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.model.Role;
import java.util.List;

public interface IRolService {
    Role getById(Long id) throws ResourceNotFoundException;
    List<Role> getAll();
}
