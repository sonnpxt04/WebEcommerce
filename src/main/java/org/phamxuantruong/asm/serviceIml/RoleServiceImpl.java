package org.phamxuantruong.asm.serviceIml;

import org.phamxuantruong.asm.dao.RoleDAO;
import org.phamxuantruong.asm.entity.Role;
import org.phamxuantruong.asm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDAO dao;
    public List<Role> findAll() {
        return dao.findAll();
    }

}
