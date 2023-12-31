package com.ukma.cleaning.order;

import com.ukma.cleaning.address.AddressEntity;
import com.ukma.cleaning.commercialProposal.CommercialProposalEntity;
import com.ukma.cleaning.review.ReviewEntity;
import com.ukma.cleaning.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class OrderEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @ManyToOne
    @JoinColumn(name = "client", nullable = false)
    private UserEntity client;

    @Column(name = "client", insertable=false, updatable=false)
    private Long clientId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "executors",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<UserEntity> executors;

    @Column(name = "comment", length = 500)
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address", nullable = false)
    private AddressEntity address;

    @OneToOne()
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "review", referencedColumnName = "id")
    private ReviewEntity review;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "duration", nullable = false)
    private Duration duration;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyJoinColumn(name = "commercial_proposal_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @CollectionTable(name = "order_commercial_proposals_mapping", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "quantity")
    private Map<CommercialProposalEntity, Integer> commercialProposals;
}
