package com.ukma.cleaning.utils.mappers;

import com.ukma.cleaning.commercialProposal.CommercialProposalDto;
import com.ukma.cleaning.commercialProposal.CommercialProposalEntity;
import com.ukma.cleaning.utils.configuration.MapperConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface CommercialProposalMapper {
    CommercialProposalDto toDto(CommercialProposalEntity proposal);

    CommercialProposalEntity toEntity(CommercialProposalDto proposal);

    List<CommercialProposalDto> toDtoList(List<CommercialProposalEntity> entities);
}
