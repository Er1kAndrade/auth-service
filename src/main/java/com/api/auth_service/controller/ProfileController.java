package com.api.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.auth_service.exception.TokenNotFoundException;
import com.api.auth_service.exception.UserNotFoundException;
import com.api.auth_service.model.UserModel;
import com.api.auth_service.repositories.UserRepository;
import com.api.auth_service.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/auth") 
public class ProfileController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/profile")
    public ResponseEntity<String> Profile(HttpServletRequest request){
       

        String token = (String) request.getAttribute("jwt");

        String Email = jwtUtil.getEmailFromToken(token);

        UserModel User = userRepository
            .findByEmail(Email)
            .orElseThrow(() -> new TokenNotFoundException("Invalid User"));


        
        // if(findByEmail())

        //  System.out.println("Email RECEBIDO NO CONTROLLER: " + Email);


        
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("");
    }
}
