package com.ukma.cleaning.review;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewDto {
    @NotNull
    private Long orderId;
    @Range(min = 1,max = 5)
    private Long cleaningRate;
    @Range(min = 1,max = 5)
    private Long employeeRate;
    private String details;
}
