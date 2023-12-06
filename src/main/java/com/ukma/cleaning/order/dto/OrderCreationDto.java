package com.ukma.cleaning.order.dto;

import com.ukma.cleaning.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCreationDto {
    private Double price;
    private LocalDateTime orderTime;
    private Long clientId;
    private String comment;
    private AddressDto address;
    private Map<Integer, Integer> proposals;
}
