package com.ukma.cleaning.order;

import com.ukma.cleaning.order.dto.*;
import com.ukma.cleaning.review.ReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order API", description = "Endpoint for operations order")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get order for user by id", description = "Get order for user by id")
    @GetMapping("/user/{id}")
    public OrderForUserDto getOrderForUser(@PathVariable Long id) {
        return orderService.getOrderByIdForUser(id);
    }

    @Operation(summary = "Get order for admin by id", description = "Get order for admin by id")
    @GetMapping("/admin/{id}")
    public OrderForAdminDto getOrderForAdmin(@PathVariable Long id) {
        return orderService.getOrderByIdForAdmin(id);
    }

    @Operation(summary = "Get all orders", description = "Get all orders")
    @GetMapping
    public List<OrderListDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Get all orders with status", description = "Get all orders with status")
    @GetMapping("/by-status/{status}")
    public List<OrderListDto> getAllOrdersByStatus(@PathVariable String status) {
        return orderService.getAllOrdersByStatus(Status.valueOf(status));
    }

    @Operation(summary = "Get all orders by user id", description = "Get all orders by user id")
    @GetMapping("/all/by-user/{id}")
    public List<OrderListDto> getAllOrdersOfUser(@PathVariable Long id) {
        return orderService.getAllOrdersByUserId(id);
    }

    @Operation(summary = "Update order for admin", description = "Update order for admin(executors, time)")
    @PutMapping("/admin")
    public OrderForAdminDto updateOrderByAdmin(@RequestBody OrderForAdminDto order) {
        return orderService.updateOrderForAdmin(order);
    }

    @Operation(summary = "Update order for user", description = "Update order for user(address, time)")
    @PutMapping("/user")
    public OrderForUserDto updateOrderByUser(@RequestBody OrderForUserDto order) {
        return orderService.updateOrderForUser(order);
    }


    @Operation(summary = "Create review for order", description = "Create review for order")
    @PutMapping("/update/review")
    public OrderForUserDto reviewOrder(@RequestBody ReviewDto review) {
        return orderService.updateReview(review);
    }

    @Operation(summary = "Create order", description = "Create order")
    @PostMapping
    public OrderForUserDto createOrder(@RequestBody OrderCreationDto order) {
        return orderService.createOrder(order);
    }

    @Operation(summary = "Delete order", description = "Delete order")
    @DeleteMapping("/{id}")
    public Boolean deleteOrder(@PathVariable Long id) {
        return orderService.cancelOrderById(id);
    }

    @GetMapping("/findAll")
    public OrderPageDto findAll(@PageableDefault(sort = {"status","creationTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return orderService.findOrdersByPage(pageable);
    }
}
