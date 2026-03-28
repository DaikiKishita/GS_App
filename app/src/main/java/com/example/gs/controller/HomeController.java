package com.example.gs.controller;

import com.example.gs.model.Objective;
import com.example.gs.model.ExpenseRecord;
import com.example.gs.model.MonthlySummary;

import com.example.gs.repository.MonthlySummaryRepository;
import com.example.gs.repository.ObjectiveRepository;
import com.example.gs.repository.ExpenseRecordRepository;
import com.example.gs.repository.MonthlySummaryRepository;

import com.example.gs.service.WeekdayCalculatorService;
import com.example.gs.service.DayCountResult;

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

import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    private final ObjectiveRepository objectiveRepository;
    private final ExpenseRecordRepository expenseRecordRepository;
    private final WeekdayCalculatorService weekdayCalculatorService;
    private final MonthlySummaryRepository monthlySummaryRepository;

    @GetMapping("")
    public String home(
        @RequestParam(required=false) LocalDate date,
        Model model
        ) {
        LocalDate now = LocalDate.now();

        long dailyAverage;
        DayOfWeek today = now.getDayOfWeek();
        final long holidayUsedMoney = 1500;

        //オブジェクト取得
        Optional<Objective> objective = objectiveRepository.findByYearAndMonth(now.getYear(),now.getMonthValue());

        //目標ない場合は遷移
        if (objective.isEmpty()){
            return "redirect:/objective/new";
        }

        //日付指定で履歴を取得
        List<ExpenseRecord> records;
        if(date==null){
            records = expenseRecordRepository.findByRecordDateOrderByRecordDateDesc(now);
            model.addAttribute("selectedDate",now);
        }else{
            records = expenseRecordRepository.findByRecordDateOrderByRecordDateDesc(date);
            model.addAttribute("selectedDate",date);
        }

        //今までの合計金額と残り日数で一日の平均使用可能金額を見積もる
        DayCountResult result = weekdayCalculatorService.countWeekdays(now);
        long totalWeekDay = result.getWeekdays();
        long totalAllDay = result.getAllDays();

        Objective obj = objective.get();

        long amountByHoliday = (totalAllDay - totalWeekDay)*holidayUsedMoney;
        long used = expenseRecordRepository.sumDailyAmount(now);
        Optional<MonthlySummary> monthlySummary = monthlySummaryRepository.findByYearAndMonth(now.getYear(),now.getMonthValue());
        long monthlyUsed = 0;
        if(!monthlySummary.isEmpty()){
            monthlyUsed = monthlySummary.get().getTotalAmount();
        }
        
        if(today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY){
            dailyAverage = holidayUsedMoney-used;
        }else{
            dailyAverage = (obj.getAmount() - (monthlyUsed + amountByHoliday))/totalWeekDay - used;
        }

        model.addAttribute("monthTotal",monthlyUsed);
        model.addAttribute("dailyAverage",dailyAverage);
        model.addAttribute("records",records);
        model.addAttribute("objective",objective);
        return "index";
    }
}
