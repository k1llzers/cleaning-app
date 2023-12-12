package com.ukma.cleaning.user.dto;

import com.ukma.cleaning.user.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDto {
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
}
