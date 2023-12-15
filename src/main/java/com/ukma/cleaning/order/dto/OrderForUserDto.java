package com.ukma.cleaning.order.dto;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.order.Status;
import com.ukma.cleaning.review.ReviewDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderForUserDto {
    @NotNull(message = "Order id can't be null")
    private Long id;
    private Double price;
    @NotNull(message = "Order time can't be null")
    private LocalDateTime orderTime;
    private AddressDto address;
    private Status status;
    private Duration duration;
    private ReviewDto review;
    private Map<String, Integer> commercialProposals;
}
