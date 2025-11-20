package com.api.auth_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.auth_service.dto.RefreshResponseDTO;
import com.api.auth_service.exception.RefreshTokenCookieEmptyException;
import com.api.auth_service.exception.TokenExpiredException;
import com.api.auth_service.exception.TokenNotFoundException;
import com.api.auth_service.model.RefreshToken;
import com.api.auth_service.model.UserModel;
import com.api.auth_service.repositories.RefreshTokenRepository;
import com.api.auth_service.security.JwtUtil;
import com.api.auth_service.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/auth") 
public class RefreshController {
                
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;    
    
    @Autowired
    private JwtUtil jwtUtil;

    
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue(value = "refresh_token", required = false) String refreshToken) {

        if (refreshToken == null) {
            throw new RefreshTokenCookieEmptyException("Refresh token cookie not found");
        }

        try{    
            RefreshToken token = refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow(() -> new TokenNotFoundException("Token not found"));

            if (token.isExpired()) {
                refreshTokenRepository.deleteById(token.getId());
                throw new TokenExpiredException("Token expired");
            } 

            UserModel user = token.getUser();

            String accessToken = jwtUtil.generateAccessToken(user.getEmail());

            RefreshToken RefreshToken = refreshTokenService.create(user);

            ResponseCookie cookie = ResponseCookie.from("refresh_token", RefreshToken.getToken())
                .httpOnly(true)
                .secure(true) // true em produção
                .path("/auth/refresh")
                .sameSite("Strict")
                .maxAge(7 * 24 * 60 * 60)
                .build();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Token resetado com sucesso!");
            response.put("access_token", accessToken);

            RefreshResponseDTO dto = new RefreshResponseDTO(
                "Token resetado com sucesso!",
                accessToken 
            );

            refreshTokenRepository.deleteById(token.getId());

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ObjectMapper().writeValueAsString(dto));

        }  catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch(Exception e){
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao logar usuário: " + e.getMessage());
        }

    }
}
