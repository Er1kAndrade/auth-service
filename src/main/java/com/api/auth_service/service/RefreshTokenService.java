package com.api.auth_service.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api.auth_service.model.RefreshToken;
import com.api.auth_service.model.UserModel;
import com.api.auth_service.repositories.RefreshTokenRepository;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration-ms}")
    private long refreshExpirationMs;

    private final RefreshTokenRepository repo;

    public RefreshTokenService(RefreshTokenRepository repo) {
        this.repo = repo;
    }

    public RefreshToken create(UserModel user) {
        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setToken(UUID.randomUUID().toString());
        rt.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
        return repo.save(rt);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repo.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            repo.delete(token);
            throw new RuntimeException("Refresh token expirado, faça login novamente");
        }
        return token;
    }
 
    // @Transactional
    // public void deleteByUser(UserModel user) {
    //     repo.deleteByUser(user);
    // }

    @Transactional
    public void deleteByUser(UserModel user) {
        if (repo.findByUser(user).isPresent()) {
            repo.deleteByUser(user);
        }
    }


}
