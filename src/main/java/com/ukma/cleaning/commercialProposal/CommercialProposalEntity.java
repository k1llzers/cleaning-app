package com.ukma.cleaning.commercialProposal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Data
@SQLDelete(sql = "UPDATE commercial_proposal SET daleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(name = "commercial_proposal")
public class CommercialProposalEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Temporal(TemporalType.TIME)
    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "count_of_employee", nullable = false)
    private Integer requiredCountOfEmployees;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ComercialProposalType type;
}
