package com.api.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProfileResponseDTO {
    private Long Id;
    private String Username;
    private String Role;
    private String Email;
}
