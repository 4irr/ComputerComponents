package com.example.gurbocoursework.Controllers;

import com.example.gurbocoursework.Model.Accessories;
import com.example.gurbocoursework.Model.Clearance;
import com.example.gurbocoursework.Model.User;
import com.example.gurbocoursework.Repositories.AccessoriesRepository;
import com.example.gurbocoursework.Repositories.ClearanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/reports")
public class ReportsController {
    @Autowired
    private ClearanceRepository operationRepository;
    @Autowired
    private AccessoriesRepository accessoriesRepository;
    @ModelAttribute(name = "user")
    public User user(@AuthenticationPrincipal User user){
        return user;
    }
    @GetMapping("/profit")
    public String getProfitReport() { return "profit"; }
    @PostMapping("/profit")
    public String processProfit(@RequestParam(name = "from") LocalDate from, @RequestParam(name = "to") LocalDate to, Model model) {
        List<Clearance> operationList = new ArrayList<>();
        operationRepository.findAll().forEach(operationList::add);
        operationList = new ArrayList<>(operationList.stream().filter(obj -> (obj.getClass().getSimpleName().equals("Sale"))).toList());
        float sum = 0;
        for(var el : operationList)
            if((el.getDate().isAfter(from) || el.getDate().equals(from)) && ((el.getDate().isBefore(to))||(el.getDate().equals(to))))
                sum += el.getAccessories().getPrice();
        model.addAttribute("profit", String.valueOf(sum));
        return "profit";
    }
    @GetMapping("/space")
    public String getSpaceReport(Model model, @RequestParam(required = false, name = "cellsnum") Integer cellsnum) {
        List<Accessories> accessoriesList = new ArrayList<>();
        List<Clearance> operationList = new ArrayList<>();
        Map<Accessories, LocalDate> accessoriesDate = new HashMap<>();
        accessoriesRepository.findAll().forEach(accessoriesList::add);
        operationRepository.findAll().forEach(operationList::add);
        operationList = new ArrayList<>(operationList.stream().filter(obj -> (obj.getClass().getSimpleName().equals("Placement"))).toList());
        accessoriesList.sort(new Comparator<Accessories>() {
            @Override
            public int compare(Accessories o1, Accessories o2) {
                return o1.getCell().compareTo(o2.getCell());
            }
        });
        for(var el : accessoriesList)
            for(var operation : operationList)
                if(operation.getAccessories().getAccessoriesID() == el.getAccessoriesID())
                    accessoriesDate.put(el, operation.getDate());
        if(cellsnum!=null)
            model.addAttribute("cellssum", cellsnum - accessoriesList.size());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        model.addAttribute("formatter", formatter);
        model.addAttribute("accessoriesDate", accessoriesDate);
        model.addAttribute("accessories", accessoriesList);
        return "space";
    }
    @GetMapping("/statistics")
    public String getStatisticsForm(){
        return "statisticsForm";
    }
    @PostMapping("/statistics")
    public String proccessStatistics(@RequestParam(name = "from") LocalDate from, @RequestParam(name = "to") LocalDate to, Model model) {
        List<Clearance> operations = new ArrayList<>();
        operationRepository.findAll().forEach(operations::add);
        operations = new ArrayList<>(operations.stream().filter(obj -> ( (obj.getDate().isAfter(from) || obj.getDate().equals(from)) && ((obj.getDate().isBefore(to))||(obj.getDate().equals(to))) )).toList());
        List<Clearance> acceptanceList = new ArrayList<>(operations.stream().filter(obj -> (obj.getClass().getSimpleName().equals("Acceptance"))).toList());
        List<Clearance> placementList = new ArrayList<>(operations.stream().filter(obj -> (obj.getClass().getSimpleName().equals("Placement"))).toList());
        List<Clearance> releaseList = new ArrayList<>(operations.stream().filter(obj -> (obj.getClass().getSimpleName().equals("Sale"))).toList());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        model.addAttribute("formatter", formatter);
        model.addAttribute("operations", operations);
        model.addAttribute("acceptanceList", acceptanceList);
        model.addAttribute("placementList", placementList);
        model.addAttribute("releasesList", releaseList);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "statistics";
    }
}
