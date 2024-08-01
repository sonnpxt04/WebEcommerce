package org.phamxuantruong.asm.service;

import org.phamxuantruong.asm.entity.Authority;

import java.util.List;

public interface AuthorityService {
    List<Authority> findAuthoritiesOfAdministrators();

    List<Authority> findAll();

    Authority create(Authority auth);

    void delete(Integer id);
}
