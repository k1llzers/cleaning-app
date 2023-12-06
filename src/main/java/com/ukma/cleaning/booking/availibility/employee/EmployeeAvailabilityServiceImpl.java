package com.ukma.cleaning.booking.availibility.employee;

import com.ukma.cleaning.order.OrderEntity;
import com.ukma.cleaning.order.OrderRepository;
import com.ukma.cleaning.order.Status;
import com.ukma.cleaning.user.Role;
import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeAvailabilityServiceImpl implements EmployeeAvailabilityService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserMapper userMapper;
    @Override
    public List<UserDto> getAllAvailableEmployees(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        LocalDateTime startTime = order.getOrderTime().minusMinutes(40);
        LocalDateTime endTime = order.getOrderTime().plusMinutes(40 + order.getDuration().toMinutes());
        List<OrderEntity> orders = orderRepository.findAllByOrderTimeBetweenAndStatusNot(startTime, endTime, Status.CANCELLED);
        List<UserEntity> allUsers = userRepository.findAllByRole(Role.Employee);
        List<UserEntity> unavailableEmployees = orders.stream()
                                                    .map(OrderEntity::getExecutors)
                                                    .flatMap(Collection::stream)
                                                    .toList();
        return allUsers.stream().filter(x -> !unavailableEmployees.contains(x)).map(userMapper::toDto).toList();
    }
}
