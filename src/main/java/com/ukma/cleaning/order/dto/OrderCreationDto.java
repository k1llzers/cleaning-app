package com.ukma.cleaning.order.dto;

import com.ukma.cleaning.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCreationDto {
    private Double price;
    private LocalDateTime orderTime;
    private Long clientId; // in service
    private String comment;
    private AddressDto address;
    // TODO CHECK HOW IT WORKS
    private Duration duration;
    private Map<Long, Integer> proposals; //in service
}
