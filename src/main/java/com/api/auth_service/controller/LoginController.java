package com.api.auth_service.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.auth_service.dto.LoginRequestDTO;
import com.api.auth_service.dto.LoginResponseDTO;
import com.api.auth_service.exception.IncorrectEmailOrPassordException;

import com.api.auth_service.exception.UserNotFoundException;
import com.api.auth_service.model.RefreshToken;
import com.api.auth_service.model.UserModel;
import com.api.auth_service.repositories.UserRepository;
import com.api.auth_service.security.JwtUtil;
import com.api.auth_service.service.RefreshTokenService;


@RestController
@RequestMapping("/auth") 
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> Login(@RequestBody LoginRequestDTO requested_User){
        String requested_user_Email = requested_User.getEmail();

        UserModel User = userRepository
            .findByEmail(requested_user_Email)
            .orElseThrow(() -> new UserNotFoundException("Invalid credentials"));

        if(!passwordEncoder.matches(requested_User.getPassword(),User.getPassword())){
            throw new IncorrectEmailOrPassordException("Senha ou nome de usuario incorreto");
        }

        refreshTokenService.deleteByUser(User);
        
        String accessToken = jwtUtil.generateAccessToken(requested_User.getEmail());

        RefreshToken refreshToken = refreshTokenService.create(User);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken.getToken())
            .httpOnly(true)
            .secure(false) 
            .path("/auth/refresh")
            .sameSite("Strict")
            .maxAge(7 * 24 * 60 * 60)
            .build();

        LoginResponseDTO dto = new LoginResponseDTO(
            "Usuario Logado com sucesso!",
            accessToken 
        );
        
        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(dto);
    }
    
}
