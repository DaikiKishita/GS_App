package com.example.gs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "expense_records")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    private Integer amount;

    @NotNull
    private LocalDate recordDate;

    @Size(max = 500)
    private String memo;

    @ManyToOne(optional = false)
    private Category category;
}
