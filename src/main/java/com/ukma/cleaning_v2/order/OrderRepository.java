package com.ukma.cleaning_v2.order;

import com.ukma.cleaning_v2.address.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findOrderEntitiesByAddress(AddressEntity address);
}
