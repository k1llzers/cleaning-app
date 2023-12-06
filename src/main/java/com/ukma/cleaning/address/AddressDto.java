package com.ukma.cleaning.address;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {
    private long id;
    @NotNull
    private String city;
    @NotNull
    private String street;
    @NotNull
    private String houseNumber;
    private String flatNumber;
    private String zip;
}
