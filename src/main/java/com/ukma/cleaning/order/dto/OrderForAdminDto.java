package com.ukma.cleaning.order.dto;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.order.Status;
import com.ukma.cleaning.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderForAdminDto {
    private Long orderId;
    private Double price;
    private LocalDateTime orderTime;
    private AddressDto address;
    private Status status;
    private Duration orderDuration;
    private Map<String, Integer> proposalsMap;
    private List<UserDto> workers;
}
