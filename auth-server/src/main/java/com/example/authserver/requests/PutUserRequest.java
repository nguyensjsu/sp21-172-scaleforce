package com.example.authserver.requests;


import lombok.Data;

@Data
public class PutUserRequest
{
    private String email;
    private String oldPassword;
    private String newPassword;
}
