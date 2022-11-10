package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccount_UserId(Long userId, Pageable pageable) throws Exception;
    List<Transaction> findByAccount_UserId(Long userId) throws Exception;
    List<Transaction> findAllByAccountId(Long accountId);

}
