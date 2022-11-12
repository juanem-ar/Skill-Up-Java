package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.FixedTermDeposit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFixedTermDepositRepository extends JpaRepository<FixedTermDeposit, Long> {

  List<FixedTermDeposit> findByAccount(Account account);
}
