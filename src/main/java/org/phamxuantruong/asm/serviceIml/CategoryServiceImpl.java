package org.phamxuantruong.asm.serviceIml;

import org.phamxuantruong.asm.dao.CategoryDAO;
import org.phamxuantruong.asm.entity.Category;
import org.phamxuantruong.asm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDAO categoryDAO;

    @Override
    public List<Category> findAll() {
        return    categoryDAO.findAll();
    }
}
