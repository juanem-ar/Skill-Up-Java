package com.alkemy.wallet.mapper;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.dto.TransactionDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper implements IValidador<Transaction> {

    @Autowired
    ITransactionRepository transactionRepository;

    @BeforeMapping
    public Transaction validador(Long id) throws ResourceNotFoundException {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new ResourceNotFoundException(msjError + id);
        }
        return transaction.get();
    }

    public abstract Transaction toEntity(TransactionDto transactionDto);
    public abstract TransactionDto toDto(Transaction transaction);

    //public RoleUser toRolUser(Role role, User user){
    //    return RolUser.builder()
    //            .rolName(role.getName())
    //            .userName(user.getName())
    //            .build();
    //}

}
