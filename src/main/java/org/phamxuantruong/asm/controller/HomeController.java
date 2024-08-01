package org.phamxuantruong.asm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping({"/", "/home/index"})
    public String index() {
        return "redirect:/product/list";
    }
    @RequestMapping({"/admin", "/admin/home/index"})
    public String admin() {
        return "redirect:/assets/admin/index.html#!/product";
    }
}
