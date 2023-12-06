package com.ukma.cleaning.user.dto;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    @NonNull
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
    @NonNull
    @NotNull(message = "Role cannot be null")
    private Role role;
    private String phoneNumber;
    private List<AddressDto> addressList;
}
