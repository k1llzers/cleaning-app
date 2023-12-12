package com.ukma.cleaning.user.dto;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.user.Role;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
    @NotNull(message = "Role cannot be null")
    private Role role;
    @Pattern(regexp = "^((\\+38\\s?)?((\\(0[1-9]{2}\\))|(0[1-9]{2}))(\\s|-)?[0-9]{3}(\\s|-)?[0-9]{2}(\\s|-)?[0-9]{2})?$", message = "Phone number should be correct")
    private String phoneNumber;
    private List<AddressDto> addressList;
}
