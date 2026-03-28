package com.example.gs.batch.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gs.repository.ExpenseRecordRepository;
import com.example.gs.repository.ObjectiveRepository;
import com.example.gs.repository.MonthlySummaryRepository;

import com.example.gs.model.MonthlySummary;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyBatchService {

    private final ExpenseRecordRepository expenseRecordRepository;
    private final ObjectiveRepository objectiveRepository;
    private final MonthlySummaryRepository monthlySummaryRepository;

    @Transactional
    public void updateMonthlySummary(){
        LocalDate now = LocalDate.now();

        MonthlySummary monthlySummary;
        Optional<MonthlySummary> monthlySummaryopt = monthlySummaryRepository.findByYearAndMonth(now.getYear(),now.getMonthValue());
        if(monthlySummaryopt.isEmpty()){
            monthlySummary = new MonthlySummary();
            monthlySummary.setYear(now.getYear());
            monthlySummary.setMonth(now.getMonthValue());
        }else{
            monthlySummary = monthlySummaryopt.get();
        }

        Integer monthlyTotalAmount = expenseRecordRepository.sumMonthlyAmount(now.getYear(),now.getMonthValue());
        monthlySummary.setTotalAmount(monthlyTotalAmount != null ? monthlyTotalAmount : 0);
        monthlySummaryRepository.save(monthlySummary);
    }
}
