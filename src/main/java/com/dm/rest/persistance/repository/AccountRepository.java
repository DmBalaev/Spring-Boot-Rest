package com.dm.rest.persistance.repository;

import com.dm.rest.persistance.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByLogin(String login);
    boolean existsByLogin(String login);
}
