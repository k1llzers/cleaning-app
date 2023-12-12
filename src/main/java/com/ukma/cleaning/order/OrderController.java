package com.ukma.cleaning.order;

import com.ukma.cleaning.order.dto.*;
import com.ukma.cleaning.review.ReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Get all orders with status (pageable)", description = "Get all orders with status (pageable)")
    @GetMapping("/all/by-status")
    public OrderPageDto getAllOrdersByStatus(@RequestParam(defaultValue = "NOT_VERIFIED") Status status,
                                                   @PageableDefault(sort = {"status","creationTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return orderService.findOrdersByStatusAndPage(status, pageable);
    }
    @Operation(summary = "Get all orders (pageable)", description = "Get all orders (pageable)")
    @GetMapping("/all")
    public OrderPageDto getAllOrders(@PageableDefault(sort = {"status","creationTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return orderService.findOrdersByPage(pageable);
    }

    @Operation(summary = "Get all orders by user id (pageable)", description = "Get all orders by user id (pageable)")
    @GetMapping("/by-user/{id}")
    public OrderPageDto getOrdersOfUser(@PathVariable Long id, @PageableDefault(sort = {"status","creationTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return orderService.findOrdersByUserId(id, pageable);
    }

    @Operation(summary = "Get all orders of executor by id (pageable)", description = "Get all orders of executor by id (pageable)")
    @GetMapping("/by-executor/{id}")
    public OrderPageDto getExecutorsOrders(@PathVariable Long id, @PageableDefault(sort = {"status","creationTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return orderService.findOrdersByExecutorId(id, pageable);
    }

    @Operation(summary = "Update order for admin", description = "Update order for admin(executors, time)")
    @PutMapping("/admin")
    public OrderForAdminDto updateOrderByAdmin(@Valid @RequestBody OrderForAdminDto order) {
        return orderService.updateOrderForAdmin(order);
    }

    @Operation(summary = "Update order for user", description = "Update order for user(address, time)")
    @PutMapping("/user")
    public OrderForUserDto updateOrderByUser(@Valid @RequestBody OrderForUserDto order) {
        return orderService.updateOrderForUser(order);
    }


    @Operation(summary = "Create review for order", description = "Create review for order")
    @PutMapping("/update/review")
    public OrderForUserDto reviewOrder(@Valid @RequestBody ReviewDto review) {
        return orderService.updateReview(review);
    }

    @Operation(summary = "Create order", description = "Create order")
    @PostMapping
    public OrderForUserDto createOrder(@Valid @RequestBody OrderCreationDto order) {
        return orderService.createOrder(order);
    }

    @Operation(summary = "Delete order", description = "Delete order")
    @DeleteMapping("/{id}")
    public Boolean deleteOrder(@PathVariable Long id) {
        return orderService.cancelOrderById(id);
    }
}
