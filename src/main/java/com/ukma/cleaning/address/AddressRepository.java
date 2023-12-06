package com.ukma.cleaning.address;

import com.ukma.cleaning.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findAddressEntitiesByUserId(Long userId);
}
