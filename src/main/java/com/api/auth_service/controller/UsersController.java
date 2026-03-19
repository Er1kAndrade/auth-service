package com.api.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.auth_service.dto.ProfileResponseDTO;
import com.api.auth_service.exception.TokenNotFoundException;
import com.api.auth_service.exception.UnathorizedAccessExeption;
import com.api.auth_service.exception.UserNotFoundException;
import com.api.auth_service.model.UserModel;
import com.api.auth_service.repositories.UserRepository;
import com.api.auth_service.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
// import io.swagger.v3.oas.annotations.parameters.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;


@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "Endpoints de usuários e perfis")
@RestController
@RequestMapping("/auth") 
public class UsersController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Operation(
        summary = "Retorna o perfil de um usuário específico",
        description = "Requer autenticação via JWT e privilégios administrativos.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou ausente"),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado (não é admin)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        }
    )

    @GetMapping("/users/{id}/profile")
    public ResponseEntity<ProfileResponseDTO> Profile(
          @Parameter(description = "ID do usuário que deseja buscar", example = "12")
          @PathVariable("id") String userid, HttpServletRequest request){
       

        String token = (String) request.getAttribute("jwt");

        String email = jwtUtil.getEmailFromToken(token);

        System.out.println(email);
            
        UserModel user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new TokenNotFoundException("Invalid User"));


        if (!user.getRole().equals("A")) {
            throw new UnathorizedAccessExeption("Admin privileges required");
        }

        long id;

        try {
            id = Long.parseLong(userid);
        } catch (NumberFormatException e) {
            throw new UserNotFoundException("Invalid ID format");
        }

        UserModel userprofile = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
            userprofile.getId(),
            userprofile.getUsername(),
            userprofile.getRole(),
            userprofile.getEmail()
        );
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(profileResponseDTO);
    }
}
