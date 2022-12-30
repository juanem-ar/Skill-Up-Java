package com.alkemy.wallet.service.generic;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.io.Serializable;

@Service
public abstract class GenericServiceImpl <T, ID extends Serializable> implements GenericServiceAPI<T, ID>{

    @Override
    public T save(T entity) {
        return getRepository().save(entity);
    }

    @Override
    public void delete(ID id) throws ResourceNotFoundException {
        getRepository().deleteById(id);
    }

    @Override
    public abstract JpaRepository<T, ID> getRepository();
}
