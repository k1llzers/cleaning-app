package com.ukma.cleaning_v2.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPasswordDto {
    private Long id;
    private String password;
}
