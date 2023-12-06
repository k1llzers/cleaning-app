package com.ukma.cleaning.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByStatusAndClientIdIs(Status status, Long clientId);

    List<OrderEntity> findAllByStatus(Status status);
}
