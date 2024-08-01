package org.phamxuantruong.asm.dao;

import org.phamxuantruong.asm.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Integer> {
}
