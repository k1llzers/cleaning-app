package com.ukma.cleaning.commercialProposal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommercialProposalDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String shortDescription;
    @NotNull
    @NotBlank
    private String fullDescription;
    @NotNull
    @NotBlank
    private String requiredCountOfEmployees;
    @NotNull
    @Positive
    private Double price;
    @NotNull
    private Duration time;
    @NotNull
    private ComercialProposalType type;
}
