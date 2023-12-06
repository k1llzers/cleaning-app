package com.ukma.cleaning.commercialProposal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommercialProposalDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Duration time;
    private ComercialProposalType type;
}
