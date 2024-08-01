package org.phamxuantruong.asm.serviceIml;

import org.phamxuantruong.asm.dao.AccountDAO;
import org.phamxuantruong.asm.dao.AuthorityDAO;
import org.phamxuantruong.asm.entity.Account;
import org.phamxuantruong.asm.entity.Authority;
import org.phamxuantruong.asm.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImp implements AuthorityService {
    @Autowired
    AuthorityDAO dao;
    @Autowired
    AccountDAO accountDAO;
    @Override
    public List<Authority> findAuthoritiesOfAdministrators() {
        List<Account> accounts = accountDAO.getAdministrators();
        return dao.authorities(accounts);
    }

    @Override
    public List<Authority> findAll() {
        return dao.findAll();
    }

    @Override
    public Authority create(Authority auth) {
        return dao.save(auth);
    }

    @Override
    public void delete(Integer id) {
        dao.deleteById(id);
    }
}
