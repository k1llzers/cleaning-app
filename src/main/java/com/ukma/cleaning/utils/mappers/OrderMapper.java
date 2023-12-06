package com.ukma.cleaning.utils.mappers;

import com.ukma.cleaning.commercialProposal.CommercialProposalEntity;
import com.ukma.cleaning.order.OrderEntity;
import com.ukma.cleaning.order.dto.OrderCreationDto;
import com.ukma.cleaning.order.dto.OrderForAdminDto;
import com.ukma.cleaning.order.dto.OrderForUserDto;
import com.ukma.cleaning.order.dto.OrderListDto;
import com.ukma.cleaning.utils.configuration.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(config = MapperConfig.class, uses = {AddressMapper.class, UserMapper.class})
public interface OrderMapper {
    @Mappings({
            @Mapping(target = "creationTime", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "status", expression = "java(com.ukma.cleaning.order.Status.NOT_VERIFIED)")
    })
    OrderEntity toEntity(OrderCreationDto order);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "commercialProposals", ignore = true),
            @Mapping(target = "orderTime", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "review", ignore = true)
    })
    void updateFields(@MappingTarget OrderEntity entity, OrderForAdminDto order);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "price", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "duration", ignore = true),
            @Mapping(target = "commercialProposals", ignore = true),
            @Mapping(target = "status", expression = "java(com.ukma.cleaning.order.Status.NOT_VERIFIED)")
    })
    void updateFields(@MappingTarget OrderEntity entity, OrderForUserDto order);

    @Mapping(target = "commercialProposals", source = "commercialProposals", qualifiedByName = "mapCommercialProposals")
    OrderForAdminDto toAdminDto(OrderEntity entity);

    @Mapping(target = "commercialProposals", source = "commercialProposals", qualifiedByName = "mapCommercialProposals")
    OrderForUserDto toUserDto(OrderEntity entity);

    List<OrderListDto> toListDto(List<OrderEntity> entity);

    @Named("mapCommercialProposals")
    default Map<String, Integer> mapCommercialProposals(Map<CommercialProposalEntity, Integer> commercialProposals) {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<CommercialProposalEntity, Integer> entry : commercialProposals.entrySet()) {
            String name = entry.getKey().getName();
            Integer value = entry.getValue();
            result.put(name, value);
        }
        return result;
    }
}
