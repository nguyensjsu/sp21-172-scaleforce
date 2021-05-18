package com.example.authserver.responses;

import lombok.Data;

import java.util.List;

@Data
public class ValidateJwtResponse
{
    private String jwt;
    private String email;
    private List<String> permissions;

    public ValidateJwtResponse(String email, List<String> permissions)
    {
        jwt = "valid";
        this.email = email;
        this.permissions = permissions;
    }
}
