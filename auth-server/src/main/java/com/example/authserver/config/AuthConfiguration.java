package com.example.authserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
//@PropertySource("classpath:application.yml")
public class AuthConfiguration
{

    @Value("${jwt.window}")
    private Integer jwtWindow;

    @Value("${jwt.key}")
    private String key;



}
