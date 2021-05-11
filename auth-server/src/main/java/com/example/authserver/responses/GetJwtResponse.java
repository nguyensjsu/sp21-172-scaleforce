package com.example.authserver.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class GetJwtResponse
{
    private String jwt;
    private String uid;

}
