package com.example.authserver.responses;

import lombok.Data;

@Data
public class ValidateJwtResponse
{
    private String jwt;

    public ValidateJwtResponse()
    {
        jwt = "valid";
    }
}
