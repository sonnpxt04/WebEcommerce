package org.phamxuantruong.asm.dao;

import org.phamxuantruong.asm.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
}
