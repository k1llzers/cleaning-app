package com.ukma.cleaning.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewDto {
    private Long id;
    private Long cleaningRate;
    private Long employeeRate;
    private String details;
}
