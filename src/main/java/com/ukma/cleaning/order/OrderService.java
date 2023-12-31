package com.ukma.cleaning.order;

import com.ukma.cleaning.order.dto.*;
import com.ukma.cleaning.review.ReviewDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderForUserDto createOrder(OrderCreationDto order);
    OrderForUserDto updateOrderForUser(OrderForUserDto order);
    OrderForAdminDto updateOrderForAdmin(OrderForAdminDto order);
    OrderForUserDto updateReview(ReviewDto order);
    OrderForUserDto getOrderByIdForUser(Long id);
    OrderForAdminDto getOrderByIdForAdmin(Long id);
    OrderForUserDto getOrderByIdForEmployee(Long id);
    Boolean cancelOrderById(Long orderId);
    List<OrderListDto> getAllOrders();
    List<OrderListDto> getAllOrdersByStatus(Status status);
    List<OrderListDto> getAllOrdersByUserId(Long id);

    OrderPageDto findOrdersByPage(Pageable pageable);
    OrderPageDto findOrdersByStatusAndPage(Status status, Pageable pageable);
    OrderPageDto findOrdersByExecutorId(Long id, Pageable pageable);
    OrderPageDto findOrdersByUserId(Long id, Pageable pageable);
    OrderListDto changeOrderStatus(Long orderId, Status status);
}
