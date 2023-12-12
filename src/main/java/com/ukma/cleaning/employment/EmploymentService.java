package com.ukma.cleaning.employment;

import java.util.List;

public interface EmploymentService {
    EmploymentDto create(String motivationList);

    Boolean succeed(Long userId);

    Boolean cancel(Long userId);

    List<EmploymentDto> getAll();
}
