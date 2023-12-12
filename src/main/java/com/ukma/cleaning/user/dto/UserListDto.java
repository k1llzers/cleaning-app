package com.ukma.cleaning.user.dto;


import com.ukma.cleaning.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserListDto {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private Role role;
}
