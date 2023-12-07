package com.ukma.cleaning.user.dto;

import com.ukma.cleaning.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
    @NonNull
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
    @NonNull
    @NotNull(message = "Role cannot be null")
    private Role role;
    private String phoneNumber;
}
