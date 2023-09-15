package com.example.gurbocoursework.Controllers;

import com.example.gurbocoursework.Builder.Director;
import com.example.gurbocoursework.Builder.OperationsBuilder;
import com.example.gurbocoursework.Model.*;
import com.example.gurbocoursework.Repositories.AccessoriesRepository;
import com.example.gurbocoursework.Repositories.ClearanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/operations")
public class ClearanceController {
    private Director director = new Director();
    private OperationsBuilder builder = new OperationsBuilder();
    @Autowired
    ClearanceRepository operationRepository;
    @Autowired
    AccessoriesRepository accessoriesRepository;
    @ModelAttribute(name = "user")
    public User user(@AuthenticationPrincipal User user){
        return user;
    }
    @GetMapping
    public String getOperations(Model model){
        List<Clearance> operations = new ArrayList<>();
        operationRepository.findAll().forEach(operations::add);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        model.addAttribute("formatter", formatter);
        model.addAttribute("operations", operations);
        return "operations";
    }
    @GetMapping("/acceptance-add")
    public String addAcceptance(Model model){
        Acceptance acceptance = new Acceptance();
        List<Accessories> accessories = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessories::add);
        model.addAttribute("accessories", accessories);
        model.addAttribute("acceptance", acceptance);
        return "operation-add";
    }
    @PostMapping("/acceptance-add")
    public String addAcceptancePost(Acceptance acceptance, @AuthenticationPrincipal User user, Model model) {
        director.makeAcceptance(builder, acceptance, user);
        Clearance acceptanceToSave = builder.getResult();
        operationRepository.save(acceptanceToSave);
        return "redirect:/operations";
    }
    @GetMapping("/placement-add")
    public String addPlacement(Model model){
        Placement placement = new Placement();
        List<Accessories> accessories = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessories::add);
        model.addAttribute("accessories", accessories);
        model.addAttribute("placement", placement);
        return "operation-add";
    }
    @PostMapping("/placement-add")
    public String addPlacementPost(Placement placement, @AuthenticationPrincipal User user, Model model) {
        director.makePlacement(builder, placement, user);
        Clearance placementToSave = builder.getResult();
        operationRepository.save(placementToSave);
        return "redirect:/operations";
    }
    @GetMapping("/release-add")
    public String addRelease(Model model){
        Sale releases = new Sale();
        List<Accessories> accessories = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessories::add);
        model.addAttribute("accessories", accessories);
        model.addAttribute("release", releases);
        return "operation-add";
    }
    @PostMapping("/release-add")
    public String addReleasePost(Sale releases, @AuthenticationPrincipal User user, Model model) {
        director.makeRelease(builder, releases, user);
        Clearance releaseToSave = builder.getResult();
        operationRepository.save(releaseToSave);
        return "redirect:/operations";
    }
    @GetMapping("/{id}")
    public String getDetails(@PathVariable(name = "id") Long id, Model model){
        Clearance operation = operationRepository.findById(id).orElseThrow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        model.addAttribute("formatter", formatter);
        model.addAttribute("res", operation);
        return "operations-details";
    }
    @GetMapping("/acceptance/{id}/edit")
    public String editAcceptanceGet(@PathVariable(name = "id") Long id, Model model){
        if(!operationRepository.existsById(id))
            return "redirect:/operations";
        Clearance acceptance = operationRepository.findById(id).orElseThrow();
        List<Accessories> accessories = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessories::add);
        model.addAttribute("accessories", accessories);
        model.addAttribute("res", acceptance);
        return "acceptance-edit";
    }
    @PostMapping("/acceptance/{id}/edit")
    public String editAcceptancePost(@PathVariable(name = "id") Long id, Acceptance acceptance, Model model){
        Clearance editAcceptance = operationRepository.findById(id).orElseThrow();
        editAcceptance.setDate(acceptance.getDate());
        editAcceptance.setAccessories(acceptance.getAccessories());
        editAcceptance.setSender(acceptance.getSender());
        operationRepository.save(editAcceptance);
        return "redirect:/operations";
    }
    @GetMapping("/placement/{id}/edit")
    public String editPlacementGet(@PathVariable(name = "id") Long id, Model model){
        if(!operationRepository.existsById(id))
            return "redirect:/operations";
        Clearance placement = operationRepository.findById(id).orElseThrow();
        List<Accessories> accessories = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessories::add);
        model.addAttribute("accessories", accessories);
        model.addAttribute("res", placement);
        return "placement-edit";
    }
    @PostMapping("/placement/{id}/edit")
    public String editPlacementPost(@PathVariable(name = "id") Long id, Placement placement, Model model){
        Clearance editPlacement = operationRepository.findById(id).orElseThrow();
        editPlacement.setDate(placement.getDate());
        editPlacement.setAccessories(placement.getAccessories());
        operationRepository.save(editPlacement);
        return "redirect:/operations";
    }
    @GetMapping("/release/{id}/edit")
    public String editReleaseGet(@PathVariable(name = "id") Long id, Model model){
        if(!operationRepository.existsById(id))
            return "redirect:/operations";
        Clearance release = operationRepository.findById(id).orElseThrow();
        List<Accessories> accessories = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessories::add);
        model.addAttribute("accessories", accessories);
        model.addAttribute("res", release);
        return "release-edit";
    }
    @PostMapping("/release/{id}/edit")
    public String editReleasePost(@PathVariable(name = "id") Long id, Sale releases, Model model){
        Clearance editRelease = operationRepository.findById(id).orElseThrow();
        editRelease.setDate(releases.getDate());
        editRelease.setAccessories(releases.getAccessories());
        operationRepository.save(editRelease);
        return "redirect:/operations";
    }
    @PostMapping("/{id}/remove")
    public String removeAcceptance(@PathVariable(name = "id") Long id) {
        operationRepository.deleteById(id);
        return "redirect:/operations";
    }
    @GetMapping("/sort")
    public String sortOperations(@RequestParam(name = "type") String type, @RequestParam(required = false, name = "date") String date, @RequestParam(required = false, name = "from") LocalDate from, @RequestParam(required = false, name = "to") LocalDate to, Model model) {
        if(date == null)
            date = "";
        List<Clearance> operations = new ArrayList<>();
        operationRepository.findAll().forEach(operations::add);
        if(!type.equals("Все"))
            operations = new ArrayList<>(operations.stream().filter(obj ->(obj.getType().equals(type))).toList());
        if(date.equals("asc"))
            operations.sort(new Comparator<Clearance>() {
                @Override
                public int compare(Clearance o1, Clearance o2) {
                    if(o1.getDate().isAfter(o2.getDate())) return 1;
                    else if (o1.getDate().isBefore(o2.getDate())) return -1;
                    else return 0;
                }
            });
        if(date.equals("desc"))
            operations.sort(new Comparator<Clearance>() {
                @Override
                public int compare(Clearance o1, Clearance o2) {
                    if(o1.getDate().isBefore(o2.getDate())) return 1;
                    else if (o1.getDate().isAfter(o2.getDate())) return -1;
                    else return 0;
                }
            });
        if(from != null && to != null)
            operations = new ArrayList<>(operations.stream().filter(obj -> ( (obj.getDate().isAfter(from) || obj.getDate().equals(from))) && ((obj.getDate().isBefore(to))||(obj.getDate().equals(to))) ).toList());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        model.addAttribute("formatter", formatter);
        model.addAttribute("operations", operations);
        return "operations";
    }
    @PostMapping("/sort/clear")
    public String sortOperationsClear() { return "redirect:/operations"; }
}

