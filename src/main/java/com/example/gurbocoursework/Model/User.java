package com.example.gurbocoursework.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Inheritance
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public abstract class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userID;
    @Size(min = 4, message = "Минимальная длина логина - 4 символа")
    private String username;
    @Size(min = 5, message = "Минимальная длина пароля - 5 символов")
    private String password;
    @Pattern(regexp = "^([а-яА-я]|[a-zA-z])*$", message = "Поле может содержать только буквы")
    private String name;
    @Pattern(regexp = "^([а-яА-я]|[a-zA-z])*$", message = "Поле может содержать только буквы")
    private String surname;
    @Pattern(regexp = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$", message = "Неверный формат почты")
    private String email;
    @Pattern(regexp = "^(\\+375)([0-9]{9})$", message = "Неверный формат номера телефона")
    private String telNum;
    private boolean isBlocked;

    public User(String username, String password, String name, String surname, String email, String telNum, boolean isBlocked){
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.telNum = telNum;
        this.isBlocked = isBlocked;
    }

    public User(User user){
        this.userID = user.userID;
        this.username = user.username;
        this.password = user.password;
        this.name = user.name;
        this.surname = user.surname;
        this.email = user.email;
        this.telNum = user.telNum;
        this.isBlocked = user.isBlocked;
    }

    public abstract User clone();
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        if(isBlocked) return false;
        else return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
