package org.phamxuantruong.asm.dao;

import org.phamxuantruong.asm.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, String> {
    @Query("select distinct ar.account from Authority ar where ar.role.id IN ('DIRE', 'STAF')")
    List<Account> getAdministrators();

    Optional<Account> findByEmail(String email);
}
