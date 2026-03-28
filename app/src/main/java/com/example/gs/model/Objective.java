package com.example.gs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "objectives")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 月の食費上限 */
    @NotNull
    @Min(0)
    private Integer amount;

    @Min(2000)
    private int year;

    @Min(1)
    @Max(12)
    private int month;

}
