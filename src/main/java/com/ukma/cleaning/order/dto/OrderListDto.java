package com.ukma.cleaning.order.dto;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.order.Status;
import com.ukma.cleaning.review.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderListDto {
    private Long id;
    private Double price;
    private LocalDateTime orderTime;
    private AddressDto address;
    private Status status;
    private ReviewDto review;
}
