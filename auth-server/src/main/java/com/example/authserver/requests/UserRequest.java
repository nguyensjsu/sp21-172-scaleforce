package com.example.authserver.requests;

import lombok.Data;

@Data
public class UserRequest
{
    private String email;
    private String password;

}
