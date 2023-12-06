package com.ukma.cleaning.booking.availibility;

import com.ukma.cleaning.order.OrderEntity;
import com.ukma.cleaning.order.OrderRepository;
import com.ukma.cleaning.order.Status;
import com.ukma.cleaning.user.Role;
import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingAvailabilityServiceImpl implements BookingAvailabilityService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public Map<LocalDate, List<LocalTime>> getAvailableTime(Long countOfExecutors, Duration duration) {
        LocalDate dateOfStart = LocalDate.now().plusDays(1);
        LocalTime timeOfStart = LocalTime.of(9, 0);
        List<UserEntity> employees = userRepository.findAllByRole(Role.Employee);
        Map<LocalDate, List<LocalTime>> dateAndTimeMap = LongStream.range(0, 7)
                .boxed()
                .collect(Collectors.toMap(
                        dateOfStart::plusDays,
                        day -> IntStream.range(0, 12)
                                .mapToObj(timeOfStart::plusHours)
                                .collect(Collectors.toList())
                ));
        dateAndTimeMap.forEach((date, timeList) -> {
            List<OrderEntity> bookedOrder = orderRepository.findAllByOrderTimeBetweenAndStatusNot(date.atStartOfDay(),
                    date.plusDays(1).atStartOfDay(), Status.CANCELLED);
            List<LocalTime> localTimes = timeList.stream()
                    .filter(time -> {
                        long countOfInvalid = bookedOrder.stream()
                                .filter(booked -> (booked.getOrderTime().toLocalTime().plusMinutes(
                                        40 + booked.getDuration().toMinutes()).isAfter(time)
                                        && booked.getOrderTime().toLocalTime()
                                        .isBefore(time.plusMinutes(duration.toMinutes())))
                                        || (booked.getOrderTime().toLocalTime().minusMinutes(40)
                                        .isBefore(time.plusMinutes(duration.toMinutes()))
                                        && booked.getOrderTime().toLocalTime().plusMinutes(booked.getDuration()
                                        .toMinutes()).isAfter(time))
                                        || (booked.getOrderTime().toLocalTime().isBefore(time)
                                        && booked.getOrderTime().toLocalTime().plusMinutes(booked.getDuration()
                                        .toMinutes()).isAfter(time)))
                                .distinct()
                                .count();
                        return employees.size() - countOfInvalid - countOfExecutors >= 0;
                    })
                    .toList();
            dateAndTimeMap.put(date, localTimes);
        });
        return dateAndTimeMap;
    }
}
