package com.example.gs.controller;

import com.example.gs.model.Objective;
import com.example.gs.model.Category;
import com.example.gs.model.ExpenseRecord;
import com.example.gs.model.MonthlySummary;
import com.example.gs.model.RecordSummary;

import com.example.gs.repository.ObjectiveRepository;
import com.example.gs.repository.CategoryRepository;
import com.example.gs.repository.ExpenseRecordRepository;
import com.example.gs.repository.MonthlySummaryRepository;

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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/record")
public class ExpenseRecordController {
    private final ObjectiveRepository objectiveRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseRecordRepository expenseRecordRepository;
    private final MonthlySummaryRepository monthlySummaryRepository;

    @GetMapping("/register")
    public String show(Model model) {
        LocalDate now = LocalDate.now();

        //オブジェクト取得
        Optional<Objective> objective = objectiveRepository.findByYearAndMonth(now.getYear(),now.getMonthValue());

        if (objective.isEmpty()){
            return "redirect:/objective/new";
        }

          List<Category> categories = categoryRepository.findAll();

        // Viewに渡す
        model.addAttribute("today",now);
        model.addAttribute("record", new ExpenseRecord());
        model.addAttribute("categories", categories);
        return "gs/register";
    }

    @PostMapping("/register")
    public String createExpenseRecord(
        @ModelAttribute ExpenseRecord record,
        @RequestParam Long categoryId
        ){

         Category category = categoryRepository
            .findById(categoryId)
            .orElseThrow();

        record.setCategory(category);
        expenseRecordRepository.save(record);
        return "redirect:/home";
    }

    @PostMapping("/delete")
    public String deleteExpenseRecord(
        @RequestParam Long recordId,
        @RequestParam String date
        ){
            expenseRecordRepository.deleteById(recordId);
            return "redirect:/home?date=" + date;
    }

    @GetMapping("/summary")
    public String getRecordSummary(
        @RequestParam(required=false) String date,
        Model model
    ){
        LocalDate now = LocalDate.now();
        YearMonth yearMonth;
        if(date==null){
            yearMonth = YearMonth.from(now);
        }else{
            yearMonth = YearMonth.parse(date);
        }

        //オブジェクト取得
        Optional<Objective> objective = objectiveRepository.findByYearAndMonth(now.getYear(),now.getMonthValue());
        //目標ない場合は遷移
        if (objective.isEmpty()){
            return "redirect:/objective/new";
        }

        List<RecordSummary> records;
        records = expenseRecordRepository.getSummaryGroupByCategories(yearMonth.getYear(),yearMonth.getMonthValue());
        
        model.addAttribute("selectedDate",yearMonth);

        Optional<MonthlySummary> monthlySummary = monthlySummaryRepository.findByYearAndMonth(yearMonth.getYear(),yearMonth.getMonthValue());
        long monthlyUsed = 0;
        if(!monthlySummary.isEmpty()){
            monthlyUsed = monthlySummary.get().getTotalAmount();
        }
        model.addAttribute("monthTotal",monthlyUsed);
        model.addAttribute("records",records);
        return "gs/summary";
    }
}
