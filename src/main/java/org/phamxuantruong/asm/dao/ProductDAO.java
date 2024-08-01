package org.phamxuantruong.asm.dao;

import org.phamxuantruong.asm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.category.id=?1")
    List<Product> findByCategoryId(String cid);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    List<Product> findByNameContaining(String name);
}
