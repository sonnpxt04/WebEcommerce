package org.phamxuantruong.asm.service;

import org.phamxuantruong.asm.entity.Account;

import java.util.List;

public interface AccountService {
    Account findById(String username);

    List<Account> getAdministrators();

    List<Account> findAll();
}
