package com.ukma.cleaning.utils.security.refresh.tokens;

import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.utils.exceptions.NoSuchEntityException;
import com.ukma.cleaning.utils.exceptions.VerifyRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String create(String username) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(userRepository.findUserEntityByEmail(username).orElseThrow(
                () -> new NoSuchEntityException("Can`t find user by email: " + username)
        ));
        String generatedRefreshToken = UUID.randomUUID().toString();
        while (findByToken(generatedRefreshToken).isPresent())
            generatedRefreshToken = UUID.randomUUID().toString();
        refreshToken.setToken(generatedRefreshToken);
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(60 * 10)); // 6 min
        return refreshTokenRepository.save(refreshToken).getToken();
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshTokenEntity verify(RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new VerifyRefreshTokenException("Can`t verify token: " + token.getToken());
        }
        return token;
    }

    public String refreshToken(String token) {
        RefreshTokenEntity oldToken = refreshTokenRepository.findByToken(token).get();
        UserEntity user = oldToken.getUser();
        refreshTokenRepository.delete(oldToken);
        return create(user.getUsername());
    }
}
