package com.ukma.cleaning.employment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employment")
@RequiredArgsConstructor()
public class EmploymentController {
    private final EmploymentService service;

    @PostMapping
    public EmploymentDto createRequest(@RequestBody String motivationList) {
        return service.create(motivationList);
    }

    @PutMapping("/employment/{userId}/succeed)")
    public Boolean succeed(@PathVariable Long userId) {
        return service.succeed(userId);
    }

    @PutMapping("/employment/{userId}/cancel)")
    public Boolean cancel(@PathVariable Long userId) {
        return service.cancel(userId);
    }

    @PutMapping("/employment/{userId}/unemployment)")
    public Boolean unemployment(@PathVariable Long userId) {
        return service.unemployment(userId);
    }

    @GetMapping
    public List<EmploymentDto> getEmploymentRequests() {
        return service.getAll();
    }
}
