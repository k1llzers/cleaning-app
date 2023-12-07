package com.ukma.cleaning.booking.availibility;

import com.ukma.cleaning.user.dto.EmployeeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ukma.cleaning.booking.availibility.employee.EmployeeAvailabilityService;
import com.ukma.cleaning.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/available")
@RequiredArgsConstructor
@Tag(name = "Availability API", description = "Endpoint for get available employee/time")
public class BookingAvailabilityController {
    private final BookingAvailabilityService bookingAvailabilityService;
    private final EmployeeAvailabilityService employeeAvailabilityService;

    @Operation(summary = "Get available time", description = "Get available time for time from next day till 7 days for required count of employees and max duration")
    @GetMapping("/time/{count}/{duration}")
    public Map<LocalDate, List<LocalTime>> getAvailable(@PathVariable Long count, @PathVariable Duration duration) {
        return bookingAvailabilityService.getAvailableTime(count, duration);
    }

    @Operation(summary = "Get available employee", description = "Get available employee for order")
    @GetMapping("/employees/{orderId}")
    public List<EmployeeDto> getAvailable(@PathVariable Long orderId) {
        return employeeAvailabilityService.getAllAvailableEmployees(orderId);
    }
}
