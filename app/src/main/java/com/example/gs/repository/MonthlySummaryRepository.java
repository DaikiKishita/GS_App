package com.example.gs.repository;

import com.example.gs.model.MonthlySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthlySummaryRepository extends JpaRepository<MonthlySummary, Long> {
    Optional<MonthlySummary> findByYearAndMonth(int year,int month);
}
