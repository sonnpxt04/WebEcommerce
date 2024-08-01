package org.phamxuantruong.asm.service;

import org.phamxuantruong.asm.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();


    Product findById(Integer id);

    List<Product> findByCategoryId(String cid);

    Product create(Product product);

    Product update(Product product);

    void delete(Integer id);

    List<Product> findByName(String name);
}
