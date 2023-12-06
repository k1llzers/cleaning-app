package com.ukma.cleaning_v2.address;

import com.ukma.cleaning_v2.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findAddressEntitiesByUser(UserEntity userEntity);
}
