package com.ukma.cleaning.order;

import com.ukma.cleaning.order.dto.OrderCreationDto;
import com.ukma.cleaning.order.dto.OrderForAdminDto;
import com.ukma.cleaning.order.dto.OrderForUserDto;
import com.ukma.cleaning.order.dto.OrderListDto;
import com.ukma.cleaning.review.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/user/{id}")
    public OrderForUserDto getOrderForUser(@PathVariable Long id) {
        return orderService.getOrderByIdForUser(id);
    }

    @GetMapping("/admin/{id}")
    public OrderForAdminDto getOrderForAdmin(@PathVariable Long id) {
        return orderService.getOrderByIdForAdmin(id);
    }

    @GetMapping
    public List<OrderListDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/all/{id}")
    public List<OrderListDto> getAllOrders(@PathVariable Long id) {
        return orderService.getAllOrdersByUserId(id);
    }


    @PutMapping("/admin")
    public OrderForAdminDto reviewOrder(@RequestBody OrderForAdminDto order) {
        return orderService.updateOrderForAdmin(order);
    }
    @PutMapping("/user")
    public OrderForUserDto reviewOrder(@RequestBody OrderForUserDto order) {
        return orderService.updateOrderForUser(order);
    }


    @PutMapping("/review")
    public OrderForUserDto reviewOrder(@RequestBody ReviewDto review) {
        return orderService.updateReview(review);
    }

    @PostMapping
    public OrderForUserDto createOrder(@RequestBody OrderCreationDto order) {
        return orderService.createOrder(order);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrderById(id);
    }
}
