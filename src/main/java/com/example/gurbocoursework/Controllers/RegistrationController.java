package com.example.gurbocoursework.Controllers;

import com.example.gurbocoursework.Model.User;
import com.example.gurbocoursework.Model.Worker;
import com.example.gurbocoursework.Repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @ModelAttribute(name = "user")
    public User user(){return new Worker();}
    @GetMapping
    public String getRegister() {
        return "registration";
    }
    @PostMapping
    public String postRegister(@Valid User user, Errors errors, Model model){
        if(errors.hasErrors())
            return "registration";
        if(userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("exists", "Пользователь с таким логином уже зарегистрирован!");
            return "registration";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setBlocked(false);
        userRepository.save(user);
        return "redirect:/login";
    }
}
