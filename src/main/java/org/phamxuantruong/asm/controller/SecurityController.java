package org.phamxuantruong.asm.controller;

import org.phamxuantruong.asm.service.AccountService;
import org.phamxuantruong.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class SecurityController {
    @Autowired
    UserService userService;
    @RequestMapping("/security/login/form")
    public String loginForm(Model model) {
        model.addAttribute("message", "Vui lòng đăng nhập");

        return "login/login";
    }
    @RequestMapping("/auth/login/success")
    public String loginSuccess(Model model, Authentication authentication) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // Thực hiện các hành động liên quan đến người dùng sau khi đăng nhập thành công
        model.addAttribute("message", "Đăng nhập thành công");

        // Gọi phương thức để lưu người dùng vào cơ sở dữ liệu
        userService.loginFromOAuth2(oauth2User);

        return "redirect:/";
    }
    @RequestMapping("/security/login/success")
    public String loginSuccess1(Model model) {
        model.addAttribute("message", "Đăng nhập thành công");
        return "redirect:/";
    }

    @RequestMapping("/security/login/error")
    public String failLogin(Model model) {
        model.addAttribute("message", "không thành công");

        return "login/login";
    }
    @RequestMapping("/security/logoff/success")
    public String logout(Model model) {
        model.addAttribute("message", "đăng xuất thành công");

        return "login/login";
    }
    @RequestMapping("/security/unauthorized")
    public String no(Model model) {
        model.addAttribute("message", "không có quyền truy xuất");

        return "login/login";
    }
}
