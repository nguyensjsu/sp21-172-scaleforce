package com.example.authserver.responses;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class ValidateJwtResponse
{
    private String jwt;
    private Collection<? extends GrantedAuthority> permissions;

    public ValidateJwtResponse(Collection<? extends GrantedAuthority> permissions)
    {
        jwt = "valid";
        this.permissions = permissions;
    }
}
