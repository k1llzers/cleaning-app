package com.ukma.cleaning.utils.mappers;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.address.AddressEntity;
import com.ukma.cleaning.utils.configuration.MapperConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    AddressEntity toEntity(AddressDto addressDto);

    AddressDto toDto(AddressEntity addressEntity);

    List<AddressDto> toListDto(List<AddressEntity> entities);
}
