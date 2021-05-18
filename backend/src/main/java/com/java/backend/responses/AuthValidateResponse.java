package com.java.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthValidateResponse
{
    private String jwt;
    private String email;
    private List<String> permissions;
}
