package com.example.gurbocoursework.Services;

import com.example.gurbocoursework.Model.Admin;
import com.example.gurbocoursework.Model.User;
import com.example.gurbocoursework.Model.Worker;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditForm {
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
    public User toUser(PasswordEncoder passwordEncoder, String role) {
        if(role.equals("Admin"))
            return new Admin(username, passwordEncoder.encode(password), name, surname, email, telNum, isBlocked);
        else
            return new Worker(username, passwordEncoder.encode(password), name, surname, email, telNum, isBlocked);
    }
}
