package com.api.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.auth_service.model.UserModel;
import com.api.auth_service.repositories.UserRepository;

@RestController
@RequestMapping("/auth") 
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserModel user){
        try{

            String HasedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(HasedPassword);
            
            System.out.println("Username: " + user.getUsername() + "\nEmail: " + user.getEmail() + "\nPassword: " + user.getPassword());

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Usuário registrado com sucesso!");
            
        } catch (Exception e) {
            e.printStackTrace();

            String message = "Erro ao registrar usuário.";

            if (e.getMessage().contains("users_username_key")) {
                message = "Este username já está em uso!";
            } else if (e.getMessage().contains("users_email_key")) {
                message = "Este email já está cadastrado!";
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }
        
    }
}
