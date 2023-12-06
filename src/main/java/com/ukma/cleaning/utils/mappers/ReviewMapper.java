package com.ukma.cleaning.utils.mappers;

import com.ukma.cleaning.review.ReviewDto;
import com.ukma.cleaning.review.ReviewEntity;
import com.ukma.cleaning.utils.configuration.MapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {
    ReviewDto toDto(ReviewEntity review);

    ReviewEntity toEntity(ReviewDto reviewDto);
}
