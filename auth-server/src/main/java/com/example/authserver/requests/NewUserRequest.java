package com.example.authserver.requests;

import lombok.Data;

@Data
public class NewUserRequest
{
    private String email;
    private String password;
    private String permission;
}