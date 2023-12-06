package com.ukma.cleaning_v2.comment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private int rate;
    @Column(length = 1000, nullable = false)
    private String text;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime date;
    //@OneToOne(mappedBy = "comment")
    //private OrderEntity order;
}
