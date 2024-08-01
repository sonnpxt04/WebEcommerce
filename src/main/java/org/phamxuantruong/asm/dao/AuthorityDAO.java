package org.phamxuantruong.asm.dao;

import org.phamxuantruong.asm.entity.Account;
import org.phamxuantruong.asm.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorityDAO extends JpaRepository<Authority, Integer> {
    @Query("select distinct a from Authority a where a.account in ?1")
    List<Authority> authorities(List<Account> accounts);
}
