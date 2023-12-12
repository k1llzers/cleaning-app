package com.ukma.cleaning.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class AddressDto {
    private Long id;
    @NotNull
    @NotBlank
    private String city;
    @NotNull
    @NotBlank
    private String street;
    @NotNull
    @NotBlank
    private String houseNumber;
    private String flatNumber;
    private String zip;
}
