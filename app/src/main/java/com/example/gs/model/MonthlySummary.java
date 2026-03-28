package com.example.gs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(
    name = "monthly_summaries",
    uniqueConstraints = @UniqueConstraint(columnNames = {"year", "month"})
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(2000)
    private int year;

    @Min(1)
    @Max(12)
    private int month;

    @NotNull
    @Min(0)
    private Integer totalAmount;
}
