package com.ukma.cleaning.commercialProposal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Duration;

@Entity
@NoArgsConstructor
@Data
@SQLDelete(sql = "UPDATE commercial_proposal SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(name = "commercial_proposal")
public class CommercialProposalEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "shortDescription", length = 100)
    private String shortDescription;

    @Column(name = "fullDescription", length = 500)
    private String fullDescription;

    @Column(name = "price", nullable = false)
    private Double price;

    @Temporal(TemporalType.TIME)
    @Column(name = "duration", nullable = false)
    private Duration time;

    @Column(name = "count_of_employee", nullable = false)
    private Integer requiredCountOfEmployees;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ComercialProposalType type;
}
