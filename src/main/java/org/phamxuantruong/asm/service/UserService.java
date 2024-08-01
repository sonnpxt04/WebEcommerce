package org.phamxuantruong.asm.service;

import org.phamxuantruong.asm.dao.AccountDAO;
import org.phamxuantruong.asm.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    PasswordEncoder pe;
@Autowired
    AccountDAO accountDAO;
    public void loginFromOAuth2(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String username = email; // Hoặc lấy username từ thông tin khác nếu cần

        // Kiểm tra xem người dùng đã tồn tại chưa
        Optional<Account> existingUser = accountDAO.findById(username);
        if (!existingUser.isPresent()) {
            // Nếu chưa tồn tại, lưu người dùng mới
            Account newUser = new Account();
            newUser.setUsername(username); // Gán username
            newUser.setFullname(name);     // Gán fullname
            newUser.setEmail(email);
            newUser.setPhoto(""); // Nếu không cần gán giá trị cho photo

            accountDAO.save(newUser);
        }

        // Tạo đối tượng UserDetails cho Spring Security
        UserDetails user = User.withUsername(username)
                .password(pe.encode(Long.toHexString(System.currentTimeMillis())))
                .roles("CUST")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }



}
