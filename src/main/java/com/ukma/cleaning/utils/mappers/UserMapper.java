package com.ukma.cleaning.utils.mappers;

import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.dto.EmployeeDto;
import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;
import com.ukma.cleaning.utils.configuration.MapperConfig;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mappings({
            @Mapping(target = "role", expression = "java(com.ukma.cleaning.user.Role.USER)"),
            @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    })
    UserEntity toEntity(UserRegistrationDto user, @Context PasswordEncoder passwordEncoder);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "addressList", ignore = true),
            @Mapping(target = "role", ignore = true)
    })
    void updateFields(@MappingTarget UserEntity entity, UserDto user);

    UserDto toDto(UserEntity user);

    EmployeeDto toEmployeeDto(UserEntity employee);

    List<EmployeeDto> toEmployeeDtoList(List<UserEntity> employees);

    @Named("encodePassword")
    default String encodePassword(String password, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }
}