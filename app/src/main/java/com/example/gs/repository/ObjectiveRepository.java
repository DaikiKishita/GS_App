package com.example.gs.repository;

import com.example.gs.model.Objective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    Optional<Objective> findByYearAndMonth(
        int year,
        int month
    );
}
