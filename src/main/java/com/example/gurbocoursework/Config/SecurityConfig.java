package com.example.gurbocoursework.Config;

import com.example.gurbocoursework.Model.User;
import com.example.gurbocoursework.Repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo){
        return username -> {
            User user = userRepo.findByUsername(username);

            if(user != null) return user;

            throw new UsernameNotFoundException("User ‘" + username + "’ not found");
        };
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/accessories/**", "/operations/**", "/reports/**", "/profile/**").authenticated()
                .requestMatchers("/admin-add").hasRole("Admin")
                .requestMatchers("/**").permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .logout()
                .and()
                .build();

    }
}
