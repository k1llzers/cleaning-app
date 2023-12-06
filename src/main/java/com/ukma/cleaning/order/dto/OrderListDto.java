package com.ukma.cleaning.order.dto;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.order.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderListDto {
    private Long orderId;
    private Double price;
    private LocalDateTime orderTime;
    private AddressDto address;
    private Status status;
}
