package com.api.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserResonseDTO {
    private Long Id;
    private Long Username;
    private Long Role;
    private Long Email;
}
