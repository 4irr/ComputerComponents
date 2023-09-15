package com.example.gurbocoursework.Model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Admin extends User{
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
    public Admin(String username, String password, String name, String surname, String email, String telNum, boolean isBlocked){
        super(username, password, name, surname, email, telNum, isBlocked);
    }
    public Admin(Admin admin){
        super(admin);
    }
    @Override
    public User clone() {
        return new Admin(this);
    }
}
