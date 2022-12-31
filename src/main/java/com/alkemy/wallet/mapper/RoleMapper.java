package com.alkemy.wallet.mapper;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.repository.IRoleRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class RoleMapper implements IValidador<Role> {
    @Autowired
    IRoleRepository roleRepository;

    @BeforeMapping
    public Role validador(Long id) throws ResourceNotFoundException {
        Optional<Role> movie = roleRepository.findById(id);
        if (movie.isEmpty()) {
            throw new ResourceNotFoundException(msjError + id);
        }
        return movie.get();
    }
}