package com.syf.develop.service;

import com.syf.develop.entity.RefreshToken;
import com.syf.develop.exception.UserEventError;
import com.syf.develop.exception.UserEventException;
import com.syf.develop.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    void validateRefreshToken(String token) throws UserEventException {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new UserEventException("Invalid refresh Token", UserEventError.INVALID_TOKEN));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
