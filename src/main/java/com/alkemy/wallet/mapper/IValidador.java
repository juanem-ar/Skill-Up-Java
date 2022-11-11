package com.alkemy.wallet.mapper;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;

public interface IValidador<T>{

    String msjError = "La búsqueda no arrojó resultados con id: ";

    T checkId(Long c) throws ResourceNotFoundException;

}
