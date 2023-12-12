package com.ukma.cleaning.order.dto;

import com.ukma.cleaning.address.AddressDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCreationDto {
    @Positive
    private Double price;
    @NotNull
    private LocalDateTime orderTime;
    @NotNull
    private Long clientId; // in service
    private String comment;
    @NotNull
    private AddressDto address;
    // TODO CHECK HOW IT WORKS
    @NotNull
    private Duration duration;
    @NotNull
    private Map<Long, Integer> proposals; //in service
}
