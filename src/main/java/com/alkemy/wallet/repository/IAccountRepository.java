package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.ECurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long>{
    List<Account> findAllByUserId(Long id);
    Long countByUserId(Long id);
    Account getReferenceByUserId(Long id);
    Account findByIdAndUserId(Long id, Long userId);
    Account getReferenceByUserIdAndCurrency(Long userId,ECurrency currency);
    Account getReferenceById(Long id);
    Account getReferenceByIdAndCurrency(Long id,ECurrency currency);
}
