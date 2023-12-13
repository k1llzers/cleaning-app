package com.ukma.cleaning.address;

import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.utils.exceptions.NoSuchEntityException;
import com.ukma.cleaning.utils.mappers.AddressMapper;
import com.ukma.cleaning.utils.security.SecurityContextAccessor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressDto create(AddressDto addressDto) {
        AddressEntity addressEntity = addressMapper.toEntity(addressDto);
        addressEntity.setUser(SecurityContextAccessor.getAuthenticatedUser());
        log.debug("Address created: " + addressEntity);
        return addressMapper.toDto(addressRepository.save(addressEntity));
    }

    @Transactional
    @Override
    public AddressDto update(AddressDto addressDto) {
        AddressEntity addressEntity = addressRepository.findById(addressDto.getId()).orElseThrow(
                () -> new NoSuchEntityException("Can`t find address by id: " + addressDto.getId())
        );
        addressRepository.delete(addressEntity);
        AddressEntity newAddress = addressMapper.toEntity(addressDto);
        newAddress.setUser(SecurityContextAccessor.getAuthenticatedUser());
        log.debug("Address updated: " + addressEntity);
        return addressMapper.toDto(addressRepository.save(newAddress));
    }

    @Override
    public Boolean deleteById(Long id) {
        log.debug("Address deleted by id: " + id);
        addressRepository.deleteById(id);
        return true;
    }

    @Override
    public AddressDto getById(Long id) {
        AddressEntity addressEntity = addressRepository.findById(id).orElseThrow(
                () -> new NoSuchEntityException("Can`t find address by id: " + id)
        );
        return addressMapper.toDto(addressEntity);
    }

    @Override
    public List<AddressDto> getUserAddresses() {
        return addressMapper.toListDto(addressRepository
                .findAddressEntitiesByUserId(SecurityContextAccessor.getAuthenticatedUserId()));
    }
}
