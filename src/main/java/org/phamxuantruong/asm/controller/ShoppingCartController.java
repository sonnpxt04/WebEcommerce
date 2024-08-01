package org.phamxuantruong.asm.controller;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShoppingCartController {
    @RequestMapping("cart/view")
    public String cartView() {
        return "cart/view";
    }
}
