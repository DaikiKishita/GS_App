package com.example.gs.controller;

import com.example.gs.model.Objective;
import com.example.gs.repository.ObjectiveRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;


@Controller
@RequiredArgsConstructor
@RequestMapping("/objective")
public class ObjectiveController {

    private final ObjectiveRepository objectiveRepository;

    @GetMapping("/new")
    public String newObjective(Model model) {

        //目的が設定されていたらhomeへ
        LocalDate now = LocalDate.now();
        if (!objectiveRepository.findByYearAndMonth(
            now.getYear(),
            now.getMonthValue()   
            ).isEmpty()){
            return "redirect:/home";
        }

        model.addAttribute("objective", new Objective());
        return "objective/new";
    }

    @PostMapping("/new")
    public String createObjective(@ModelAttribute Objective objective) {
        
        //現在の年、月保管
        LocalDate now = LocalDate.now();
        objective.setYear(now.getYear());
        objective.setMonth(now.getMonthValue());

        objectiveRepository.save(objective);

        return "redirect:/home";
    }
}
