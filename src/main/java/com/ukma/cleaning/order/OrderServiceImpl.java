package com.ukma.cleaning.order;

import com.ukma.cleaning.order.dto.OrderCreationDto;
import com.ukma.cleaning.order.dto.OrderForAdminDto;
import com.ukma.cleaning.order.dto.OrderForUserDto;
import com.ukma.cleaning.order.dto.OrderListDto;
import com.ukma.cleaning.review.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public OrderForUserDto createOrder(OrderCreationDto order) {
        return null;
    }

    @Override
    public OrderForUserDto updateOrderForUser(OrderForUserDto order) {
        return null;
    }

    @Override
    public OrderForAdminDto updateOrderForAdmin(OrderForAdminDto order) {
        return null;
    }

    @Override
    public OrderForUserDto updateReview(ReviewDto order) {
        return null;
    }

    @Override
    public OrderForUserDto getOrderByIdForUser(Long id) {
        return null;
    }

    @Override
    public OrderForAdminDto getOrderByIdForAdmin(Long id) {
        return null;
    }

    @Override
    public Boolean deleteOrderById(Long orderId) {
        return null;
    }

    @Override
    public List<OrderListDto> getAllOrders() {
        return null;
    }

    @Override
    public List<OrderListDto> getAllOrdersByUserId(Long id) {
        return null;
    }
}
