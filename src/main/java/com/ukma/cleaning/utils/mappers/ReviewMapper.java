package com.ukma.cleaning.utils.mappers;

import com.ukma.cleaning.review.ReviewDto;
import com.ukma.cleaning.review.ReviewEntity;
import com.ukma.cleaning.utils.configuration.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {
    @Mapping(target = "orderId", source = "id")
    ReviewDto toDto(ReviewEntity review);

    ReviewEntity toEntity(ReviewDto reviewDto);
}
