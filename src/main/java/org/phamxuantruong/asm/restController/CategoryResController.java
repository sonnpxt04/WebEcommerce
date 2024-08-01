package org.phamxuantruong.asm.restController;

import org.phamxuantruong.asm.entity.Category;
import org.phamxuantruong.asm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoryResController {
    @Autowired
    CategoryService categoryService;
    @GetMapping()
    public List<Category> getAll() {
            return categoryService.findAll();
    }


}
