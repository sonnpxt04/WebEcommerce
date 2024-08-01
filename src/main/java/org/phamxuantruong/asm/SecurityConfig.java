package org.phamxuantruong.asm;

import org.phamxuantruong.asm.entity.Account;
import org.phamxuantruong.asm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;


import java.util.NoSuchElementException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AccountService accountService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/order/**").authenticated()
                                .requestMatchers("/admin/**").hasAnyRole("STAF", "DIRE")
                                .requestMatchers("/rest/authorities").hasRole("DIRE")
                                .requestMatchers("/api/payment/**").permitAll()
                                .anyRequest().permitAll()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/security/login/form")
                                .loginProcessingUrl("/security/login")
                                .defaultSuccessUrl("/security/login/success", false)
                                .failureUrl("/security/login/error")
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/auth/login/form")
                                .defaultSuccessUrl("/auth/login/success", false)
                                .failureUrl("/security/login/error")

                )


                .rememberMe(rememberMe ->
                        rememberMe
                                .tokenValiditySeconds(86400)
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/security/unauthorized")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/security/logoff")
                                .logoutSuccessUrl("/security/logoff/success")
                                .invalidateHttpSession(true)  // Xóa session
                                .deleteCookies("JSESSIONID")  // Xóa cookie phiên nếu cần
                )
                .csrf(csrf -> csrf.disable());  // Vô hiệu hóa CSRF nếu cần thiết

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return username -> {
            try {
                Account user = accountService.findById(username);
                String password = user.getPassword();  // Sử dụng mật khẩu gốc từ cơ sở dữ liệu
                String[] roles = user.getAuthorities().stream()
                        .map(er -> er.getRole().getId())
                        .toArray(String[]::new);
                return User.withUsername(username)
                        .password(password)
                        .roles(roles)  // Các vai trò ở đây cần phải có tiền tố ROLE_
                        .build();
            } catch (NoSuchElementException e) {
                throw new UsernameNotFoundException(username + " not found");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(HttpMethod.OPTIONS, "/");
    }
}
