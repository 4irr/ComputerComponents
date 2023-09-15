package com.example.gurbocoursework.Controllers;

import com.example.gurbocoursework.Model.Admin;
import com.example.gurbocoursework.Model.Clearance;
import com.example.gurbocoursework.Model.User;
import com.example.gurbocoursework.Repositories.ClearanceRepository;
import com.example.gurbocoursework.Repositories.UserRepository;
import com.example.gurbocoursework.Services.UserEditForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@SessionAttributes("user")
public class MainController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ClearanceRepository operationRepository;
    @Autowired
    PasswordEncoder encoder;

    @ModelAttribute(name = "user")
    public User user(@AuthenticationPrincipal User user) {
        return user;
    }

    @ModelAttribute(name = "admin")
    public Admin admin() { return new Admin(); }

    @ModelAttribute(name = "map")
    public Map<String, String> map(){
        Map<String, String> map = new HashMap<>();
        map.put("главная", "home");
        map.put("операции", "redirect:/operations");
        map.put("комплектующие", "redirect:/accessories");
        map.put("контакты", "contacts");
        map.put("вход", "login");
        map.put("регистрация", "redirect:/registration");
        map.put("профиль", "redirect:/profile");
        return map;
    }

    @GetMapping("/")
    public String getMain() {
        return "home";
    }

    @GetMapping("/home")
    public String getHome(@AuthenticationPrincipal User user) {
        System.out.println(user.getSurname());
        return "home";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/contacts")
    public String getContacts() {
        return "contacts";
    }

    @GetMapping("/profile")
    public String getProfile(@ModelAttribute(name = "user") User user, Model model) {
        model.addAttribute("user", userRepository.findByUsername(user.getUsername()));
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileGet(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("editUser", user.clone());
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String editProfilePost(@AuthenticationPrincipal User user, @Valid User editUser, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("editUser", user.clone());
            model.addAttribute("error", "Данные введены неверно!");
            return "profile-edit";
        }
        User userToEdit = userRepository.findById(user.getUserID()).orElseThrow();
        userToEdit.setName(editUser.getName());
        userToEdit.setSurname(editUser.getSurname());
        userToEdit.setEmail(editUser.getEmail());
        userToEdit.setTelNum(editUser.getTelNum());
        userRepository.save(userToEdit);
        model.addAttribute("success", "Данные были изменены успешно");
        return "profile";
    }
    @GetMapping("/profile/change-password")
    public String changePasswordGet() {
        return "change-password";
    }
    @PostMapping("/profile/change-password")
    public String postChangePassword(@AuthenticationPrincipal User user, @RequestParam(name = "password") String password, @RequestParam(name = "repPassword") String repPassword, Model model) {
        User userToEdit = user.clone();
        if (!password.equals(repPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "change-password";
        }
        User editUser = userRepository.findById(user.getUserID()).orElseThrow();
        editUser.setPassword(encoder.encode(password));
        userRepository.save(editUser);
        model.addAttribute("success", "Данные были изменены успешно");
        return "profile";
    }
    @GetMapping("/profile/delete-account")
    public String deleteAccountGet(){
        return "deleteAccount";
    }
    @PostMapping("/profile/delete-account")
    public String deleteAccountPost(@ModelAttribute(name = "user") User user, Model model) {
        List<Clearance> operations = new ArrayList<>();
        operationRepository.findAll().forEach(operations::add);
        for(var el : operations)
            if(el.getUser().getUserID().equals(user.getUserID())) {
                model.addAttribute("exists", "Перед удалением аккаунта необходимо очистить список всех операций, связанных с данным пользователем!");
                return "deleteAccount";
            }
        User userToDelete = user.clone();
        userRepository.deleteById(userToDelete.getUserID());
        model.addAttribute("user", null);
        return "home";
    }
    @GetMapping("/add-admin")
    public String addAdminGet(){
        return "admin-add";
    }
    @PostMapping("/admin-add")
    public String addAdminPost(@Valid Admin admin, Errors errors, Model model){
        if(errors.hasErrors())
            return "admin-add";
        if(userRepository.findByUsername(admin.getUsername())!=null) {
            model.addAttribute("exists", "Пользователь с таким логином уже существует!");
            return "admin-add";
        }
        admin.setPassword(encoder.encode(admin.getPassword()));
        userRepository.save(admin);
        return "profile";
    }
    @GetMapping("/users")
    public String getUsers(@AuthenticationPrincipal User user, Model model) {
        User authorizedUser = userRepository.findById(user.getUserID()).orElseThrow();
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        users.remove(authorizedUser);
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/users/{id}")
    public String getUserDetails(@PathVariable(name = "id") Long id, Model model) {
        if(!userRepository.existsById(id))
            return "redirect:/users";
        model.addAttribute("res", (User)userRepository.findById(id).orElseThrow());
        return "user-details";
    }
    @GetMapping("/users/{id}/edit")
    public String getUserEdit(@PathVariable(name = "id") Long id, Model model) {
        if(!userRepository.existsById(id))
            return "redirect:/users";
        User res = userRepository.findById(id).orElseThrow();
        model.addAttribute("res", res);
        model.addAttribute("role", res.getClass().getSimpleName());
        return "user-edit";
    }
    @PostMapping("/users/{id}/edit")
    public String proccessUserEdit(@Valid UserEditForm userForm, Errors errors, @PathVariable(name = "id") Long id, @RequestParam(name = "role") String role, Model model) {
        if(errors.hasErrors()) {
            User res = userRepository.findById(id).orElseThrow();
            model.addAttribute("res", res);
            model.addAttribute("role", res.getClass().getSimpleName());
            model.addAttribute("error", "Данные введены неверно!");
            return "user-edit";
        }
        User editUser = userRepository.findById(id).orElseThrow();
        editUser.setUsername(userForm.getUsername());
        editUser.setPassword(encoder.encode(userForm.getPassword()));
        editUser.setName(userForm.getName());
        editUser.setSurname(userForm.getSurname());
        editUser.setEmail(userForm.getEmail());
        editUser.setTelNum(userForm.getTelNum());
        userRepository.save(editUser);
        return "redirect:/users";
    }
    @PostMapping("/users/{id}/remove")
    public String deleteUser(@PathVariable(name = "id") Long id, Model model) {
        User deleteUser = userRepository.findById(id).orElseThrow();
        List<Clearance> operations = new ArrayList<>();
        operationRepository.findAll().forEach(operations::add);
        for(var el : operations)
            if(el.getUser().getUserID().equals(deleteUser.getUserID())) {
                model.addAttribute("exists", "Перед удалением аккаунта необходимо очистить список всех операций, связанных с данным пользователем!");
                model.addAttribute("res", (User)userRepository.findById(id).orElseThrow());
                return "user-details";
            }
        userRepository.delete(deleteUser);
        return "redirect:/users";
    }
    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable(name = "id") Long id, @AuthenticationPrincipal User user, Model model){
        User userToBlock = userRepository.findById(id).orElseThrow();
        if(!userToBlock.isBlocked())
            userToBlock.setBlocked(true);
        userRepository.save(userToBlock);
        User authorizedUser = userRepository.findById(user.getUserID()).orElseThrow();
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        users.remove(authorizedUser);
        model.addAttribute("users", users);
        return "users";
    }
    @PostMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable(name = "id") Long id){
        User userToUnblock = userRepository.findById(id).orElseThrow();
        if(userToUnblock.isBlocked())
            userToUnblock.setBlocked(false);
        userRepository.save(userToUnblock);
        return "redirect:/users";
    }
    @GetMapping("/search")
    public String processSearch(@RequestParam(name = "value") String value, @ModelAttribute(name = "map") HashMap<String, String> map, Model model) {
        value = value.trim().toLowerCase();
        if(map.get(value) == null)
            return "notFound";
        return map.get(value);
    }
}
