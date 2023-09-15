package com.example.gurbocoursework.Controllers;

import com.example.gurbocoursework.Model.Accessories;
import com.example.gurbocoursework.Model.Clearance;
import com.example.gurbocoursework.Model.User;
import com.example.gurbocoursework.Repositories.AccessoriesRepository;
import com.example.gurbocoursework.Repositories.ClearanceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/accessories")
public class AccessoriesController {
    @Autowired
    private AccessoriesRepository accessoriesRepository;
    @Autowired
    private ClearanceRepository operationRepository;
    @ModelAttribute(name = "user")
    public User user(@AuthenticationPrincipal User user){
        return user;
    }
    @ModelAttribute(name = "accessories")
    private Accessories accessories() { return new Accessories(); }
    @GetMapping
    public String getAccessories(Model model){
        List<Accessories> accessoriesList = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessoriesList::add);
        model.addAttribute("accessoriesList", accessoriesList);
        return "accessories";
    }
    @GetMapping("/add")
    public String addAccessories(Model model){
        return "accessories-add";
    }
    @PostMapping("/add")
    public String addAccessoriesPost(@Valid Accessories accessories, Errors errors, Model model) {
        if(errors.hasErrors())
            return "accessories-add";
        accessoriesRepository.save(accessories);
        List<Accessories> accessoriesList = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessoriesList::add);
        model.addAttribute("accessoriesList", accessoriesList);
        return "accessories";
    }
    @GetMapping("/{id}")
    public String accessoriesDetails(@PathVariable(name = "id") Long id, Model model){
        Accessories accessories = accessoriesRepository.findById(id).orElseThrow();
        model.addAttribute("res", accessories);
        return "accessories-details";
    }
    @GetMapping("/{id}/edit")
    public String editAccessoriesGet(@PathVariable(name = "id") Long id, Model model){
        Accessories accessories = accessoriesRepository.findById(id).orElseThrow();
        model.addAttribute("res", accessories);
        return "accessories-edit";
    }
    @PostMapping("/{id}/edit")
    public String editAccessoriesPost(@PathVariable(name = "id") Long id, @Valid Accessories res, Errors errors,  Model model){
        Accessories accessories = accessoriesRepository.findById(id).orElseThrow();
        if(errors.hasErrors()) {
            model.addAttribute("res", accessories);
            model.addAttribute("error", "Данные введены неверно");
            return "accessories-edit";
        }
        accessories.setType(res.getType());
        accessories.setName(res.getName());
        accessories.setCell(res.getCell());
        accessories.setPrice(res.getPrice());
        accessories.setWeight(res.getWeight());
        accessoriesRepository.save(accessories);
        return "redirect:/accessories/" + accessories.getAccessoriesID();
    }
    @PostMapping("/{id}/remove")
    public String removeAccessories(@PathVariable(name = "id") Long id, Model model) {
        if(!accessoriesRepository.existsById(id))
            return "redirect:/accessories";
        List<Clearance> operations = new ArrayList<>();
        operationRepository.findAll().forEach(operations::add);
        for(var el : operations)
            if(el.getAccessories().getAccessoriesID() == id) {
                model.addAttribute("exists", "Перед удалением данного товара необходимо очистить список связанных операций!");
                Accessories accessories = accessoriesRepository.findById(id).orElseThrow();
                model.addAttribute("res", accessories);
                return "accessories-details";
            }
        accessoriesRepository.deleteById(id);
        List<Accessories> accessoriesList = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessoriesList::add);
        model.addAttribute("accessoriesList", accessoriesList);
        return "accessories";
    }
    @GetMapping("/sort")
    public String sortAccessories(@RequestParam(required = false, name = "type") String type, @RequestParam(required = false, name = "price") String price, @RequestParam(required = false, name = "from") Float from, @RequestParam(required = false, name = "to") Float to, Model model){
        if(price==null)
            price = "";
        List<Accessories> accessories = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessories::add);
        if(type!="")
            accessories = new ArrayList<>(accessories.stream().filter(obj -> (obj.getType().toLowerCase().trim().equals(type.trim().toLowerCase()))).toList());
        if(price.equals("asc"))
            accessories.sort(new Comparator<Accessories>() {
                @Override
                public int compare(Accessories o1, Accessories o2) {
                    if(o1.getPrice() > o2.getPrice()) return 1;
                    else if(o1.getPrice() < o2.getPrice()) return -1;
                    else return 0;
                }
            });
        if(price.equals("desc"))
            accessories.sort(new Comparator<Accessories>() {
                @Override
                public int compare(Accessories o1, Accessories o2) {
                    if(o1.getPrice() < o2.getPrice()) return 1;
                    else if(o1.getPrice() > o2.getPrice()) return -1;
                    else return 0;
                }
            });
        if(from != null && to != null)
            accessories = new ArrayList<>(accessories.stream().filter(obj -> (obj.getPrice() >= from && obj.getPrice() <= to)).toList());
        model.addAttribute("accessoriesList", accessories);
        return "accessories";
    }
    @PostMapping("/sort/clear")
    public String sortAccessoriesClear(Model model){
        return "redirect:/accessories";
    }
}
