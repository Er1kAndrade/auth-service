package com.api.auth_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.auth_service.model.RefreshToken;
import com.api.auth_service.model.UserModel;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(UserModel user);
    void deleteByUser(UserModel user);
    

};

