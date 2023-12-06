package com.ukma.cleaning.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    // for user to get his orders
    List<OrderEntity> findAllByStatusNotAndClientIdIs(Status status, Long clientId);
    // for admin to get all needed orders by status
    List<OrderEntity> findAllByStatus(Status status);
    // for available booking time
    List<OrderEntity> findAllByOrderTimeBetween(LocalDateTime start, LocalDateTime end);
}
