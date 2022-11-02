package com.alkemy.wallet.security.repository;

import com.alkemy.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
}
