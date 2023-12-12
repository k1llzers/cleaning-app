package com.ukma.cleaning.order.dto;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.order.Status;
import com.ukma.cleaning.review.ReviewDto;
import com.ukma.cleaning.user.dto.EmployeeDto;
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
    private Long id;
    private Double price;
    private LocalDateTime orderTime;
    private AddressDto address;
    private Status status;
    private Duration duration;
    private ReviewDto review;
    private Map<String, Integer> commercialProposals;
    private List<EmployeeDto> executors;
}
