package com.ukma.cleaning.booking.availibility.employee;

import com.ukma.cleaning.user.dto.EmployeeDto;

import java.util.List;

public interface EmployeeAvailabilityService {
    List<EmployeeDto> getAllAvailableEmployees(Long orderId);
}
