package com.ukma.cleaning.order.dto;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.order.Status;
import com.ukma.cleaning.review.ReviewDto;
import com.ukma.cleaning.user.dto.EmployeeDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderForAdminDto {
    @NotNull
    private Long id;
    @Positive
    private Double price;
    private LocalDateTime orderTime;
    private AddressDto address;
    @NotNull
    private Status status;
    @NotNull
    private Duration duration;
    private ReviewDto review;
    private Map<String, Integer> commercialProposals;
    @NotNull
    @Size(min = 1)
    private List<EmployeeDto> executors;
}
