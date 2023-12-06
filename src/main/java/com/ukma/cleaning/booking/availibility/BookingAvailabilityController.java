package com.ukma.cleaning.booking.availibility;

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
public class BookingAvailabilityController {
    private final BookingAvailabilityService bookingAvailabilityService;

    @GetMapping("/time/{count}/{duration}")
    public Map<LocalDate, List<LocalTime>> getAvailable(@PathVariable Long count, @PathVariable Duration duration) {
        return bookingAvailabilityService.getAvailableTime(count, duration);
    }
}
