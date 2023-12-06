package com.ukma.cleaning.booking.availibility.employee;

import com.ukma.cleaning.user.dto.UserDto;

import java.util.List;

public interface EmployeeAvailabilityService {
    List<UserDto> getAllAvailableEmployees(Long orderId);
}
