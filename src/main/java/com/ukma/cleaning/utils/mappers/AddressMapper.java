package com.ukma.cleaning.utils.mappers;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.address.AddressEntity;
import com.ukma.cleaning.utils.configuration.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    AddressEntity toEntity(AddressDto addressDto);

    AddressDto toDto(AddressEntity addressEntity);

    List<AddressDto> toListDto(List<AddressEntity> entities);
}