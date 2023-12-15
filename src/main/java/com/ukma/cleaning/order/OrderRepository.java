package com.ukma.cleaning.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    // for user to get his orders
    List<OrderEntity> findAllByStatusNotAndClientIdIs(Status status, Long clientId);
    // for admin to get all needed orders by status
    List<OrderEntity> findAllByStatus(Status status);
    // for available booking time
    List<OrderEntity> findAllByOrderTimeBetweenAndStatusNot(LocalDateTime start, LocalDateTime end, Status status);
    Page<OrderEntity> findAll(Pageable pageable);
    Page<OrderEntity> findAllByStatus(Status status, Pageable pageable);
    Page<OrderEntity> findOrdersByExecutorsId(Long executorId, Pageable pageable);
    List<OrderEntity> findOrdersByExecutorsId(Long executorId);
    Page<OrderEntity> findOrdersByClientId(Long clientId, Pageable pageable);
}
