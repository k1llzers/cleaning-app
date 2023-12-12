package com.ukma.cleaning.utils.mappers;

import com.ukma.cleaning.employment.EmploymentDto;
import com.ukma.cleaning.employment.EmploymentEntity;
import com.ukma.cleaning.utils.configuration.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class})
public interface EmploymentMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "applicant", ignore = true),
            @Mapping(target = "creationTime", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "motivationList", source = "motivationList")
    })
    EmploymentEntity toEntity(String motivationList);

    EmploymentDto toDto(EmploymentEntity entity);

    List<EmploymentDto> toDtoList(List<EmploymentEntity> entities);
}
