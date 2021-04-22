package com.example.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthProperties
{

    private int window;
    private String key;

    @Autowired
    public AuthProperties(@Value("${jwt.window}") Integer window,
                          @Value("${jwt.key}") String key)
    {
        this.window = window;
        this.key = key;
    }

    public int getWindow()
    {
        return window;
    }

    public String getKey()
    {
        return key;
    }
}
