package com.example.gs.service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import java.time.temporal.TemporalAdjusters;

@Service
public class WeekdayCalculatorService {

    public DayCountResult countWeekdays(LocalDate today) {
        LocalDate end = today.with(TemporalAdjusters.lastDayOfMonth());
        if (today.isAfter(end)) {
            return new DayCountResult(0,0);
        }

        long weekdays = 0;
        long alldays=0;
        LocalDate current = today;

        while (!current.isAfter(end)) {
            alldays++;
            DayOfWeek dow = current.getDayOfWeek();
            // 土日以外をカウント
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                weekdays++;
            }
            current = current.plusDays(1);
        }
        return new DayCountResult(weekdays,alldays);
    }
}
