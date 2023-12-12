package com.ukma.cleaning.employment;

import com.ukma.cleaning.user.dto.EmployeeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class EmploymentDto {
    private Long id;
    private EmployeeDto applicant;
    private LocalDateTime creationTime;
    @NotBlank(message = "List can`t be blank")
    @Size(max = 1000, message = "List can`t be more than 1000 characters")
    private String motivationList;
}
