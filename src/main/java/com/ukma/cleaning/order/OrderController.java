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

    @GetMapping("/by-status/{status}")
    public List<OrderListDto> getAllOrdersByStatus(@PathVariable String status) {
        return orderService.getAllOrdersByStatus(Status.valueOf(status));
    }

    @GetMapping("/all/by-user/{id}")
    public List<OrderListDto> getAllOrdersOfUser(@PathVariable Long id) {
        return orderService.getAllOrdersByUserId(id);
    }

    @PutMapping("/admin")
    public OrderForAdminDto updateOrderByAdmin(@RequestBody OrderForAdminDto order) {
        return orderService.updateOrderForAdmin(order);
    }

    @PutMapping("/user")
    public OrderForUserDto updateOrderByUser(@RequestBody OrderForUserDto order) {
        return orderService.updateOrderForUser(order);
    }


    @PutMapping("/update/review")
    public OrderForUserDto reviewOrder(@RequestBody ReviewDto review) {
        return orderService.updateReview(review);
    }

    @PostMapping
    public OrderForUserDto createOrder(@RequestBody OrderCreationDto order) {
        return orderService.createOrder(order);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteOrder(@PathVariable Long id) {
        return orderService.cancelOrderById(id);
    }
}
