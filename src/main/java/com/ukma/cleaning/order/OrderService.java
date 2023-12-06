package com.ukma.cleaning.order;

import com.ukma.cleaning.order.dto.OrderCreationDto;
import com.ukma.cleaning.order.dto.OrderForAdminDto;
import com.ukma.cleaning.order.dto.OrderForUserDto;
import com.ukma.cleaning.order.dto.OrderListDto;

import java.util.List;

public interface OrderService {
    OrderForUserDto createOrder(OrderCreationDto order);
    OrderForUserDto editOrderForUser(OrderForUserDto order);
    OrderForAdminDto editOrderForAdmin(OrderForAdminDto order);
    OrderForUserDto getOrderByIdForUser(Long id);
    OrderForAdminDto getOrderByIdForAdmin(Long id);
    Boolean deleteOrder(Long orderId);
    List<OrderListDto> getAllOrders();
    List<OrderListDto> getAllOrdersByUserId(Long id);
}
