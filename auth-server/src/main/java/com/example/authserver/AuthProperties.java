package com.example.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthProperties
{

    private int window;

    // This key must be base64 encoded
    private String key;

    private String issuer;

    @Autowired
    public AuthProperties(@Value("${jwt.window}") Integer window,
                          @Value("${jwt.key}") String key,
                          @Value("${jwt.issuer}")String issuer)
    {
        this.window = window;
        this.key = key;
        this.issuer = issuer;
    }

    public int getWindow()
    {
        return window;
    }

    public String getKey()
    {
        return key;
    }

    public String getIssuer()
    {
        return issuer;
    }
}
