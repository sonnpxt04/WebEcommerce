package org.phamxuantruong.asm.controller;

import org.phamxuantruong.asm.entity.Product;
import org.phamxuantruong.asm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;
    @RequestMapping("product/list")
    public String list(Model model, @RequestParam("cid") Optional<String> cid,
                       @RequestParam("search") Optional<String> search
                       ) {
        List<Product> list;
        if (search.isPresent()) {
            list = productService.findByName(search.get());
        } else if (cid.isPresent()) {
            list = productService.findByCategoryId(cid.get());
        } else {
            list = productService.findAll();
        }
        model.addAttribute("items", list);
        return "product/list";
    }



    @RequestMapping("product/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Product item = productService.findById(id);
        model.addAttribute("item", item);
        return "product/detail";
    }

}
