package org.phamxuantruong.asm.restController;

import org.phamxuantruong.asm.entity.Product;
import org.phamxuantruong.asm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ProductResController {
    @Autowired
    ProductService productService;
    @GetMapping()
    public List<Product> getAll() {
            return productService.findAll();
    }

    @GetMapping("{id}")
    public Product getOne(@PathVariable("id") Integer id) {
        return productService.findById(id);
    }
    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }
    @PutMapping("{id}")
    public Product update(@PathVariable("id") Integer id, @RequestBody Product product) {
        return productService.update(product);
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id) {
         productService.delete(id);
    }
}
