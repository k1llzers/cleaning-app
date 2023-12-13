package com.ukma.cleaning.utils.security.refresh.tokens;

import com.ukma.cleaning.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String token;

    private Instant expiryDate;

    @OneToOne
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
