package com.ukma.cleaning.utils.mappers;

import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.dto.UserRegistrationDto;
import com.ukma.cleaning.utils.configuration.MapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserEntity toEntity(UserRegistrationDto user);
}
