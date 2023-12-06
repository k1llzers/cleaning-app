package com.ukma.cleaning_v2.review;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "review")
public class ReviewEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cleaning_rate", nullable = false)
    private Long cleaningRate;

    @Column(name = "employee_rate", nullable = false)
    private Long employeeRate;

    @Column(name = "details", length = 700)
    private String details;
}
