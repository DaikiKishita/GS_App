package com.example.gs.repository;

import com.example.gs.model.ExpenseRecord;
import com.example.gs.model.RecordSummary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


public interface ExpenseRecordRepository extends JpaRepository<ExpenseRecord, Long> {
    List<ExpenseRecord> findByRecordDateOrderByRecordDateDesc(LocalDate recordDate);
    
    @Query("""
    SELECT COALESCE(SUM(r.amount), 0)
    FROM ExpenseRecord r
    WHERE YEAR(r.recordDate) = :year
      AND MONTH(r.recordDate) = :month
      AND r.category.id <> 5
    """)
    Integer sumMonthlyAmount(
        @Param("year") int year,
        @Param("month") int month
    );

    @Query("""
    SELECT COALESCE(SUM(r.amount), 0)
    FROM ExpenseRecord r
    WHERE r.recordDate = :date
    AND r.category.id <> 5
    AND r.category.id <> 1
    """)
    Integer sumDailyAmount(
        @Param("date") LocalDate date
    );

    @Query("""
    SELECT SUM(r.amount) as amount,r.category.name as categoryName
    FROM ExpenseRecord r
    WHERE YEAR(r.recordDate) = :year
    AND MONTH(r.recordDate) = :month
    GROUP BY r.category.id,r.category.name
    """)
    List<RecordSummary> getSummaryGroupByCategories(
        @Param("year") int year,
        @Param("month") int month
    );
}
