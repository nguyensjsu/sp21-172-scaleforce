package com.example.authserver.util;

import com.example.authserver.AuthProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtWrapper
{
    private AuthProperties authProperties;

    @Autowired
    public JwtWrapper(AuthProperties properties)
    {
        this.authProperties = properties;
    }

    public String buildJws()
    {

        System.out.println(authProperties.getKey());
        System.out.println(authProperties.getWindow());

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder()
                .setIssuer("ME")
                .setSubject("For testing")
                .claim("name", "Username")
                .claim("Haircut", "User")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(authProperties.getWindow())))
                .signWith(key)
                .compact();
    }

//    Byte[] stuff = {-50, -98, 27, -44, -52, 124, 120, -79, 94, -72, 14, 8, 53, 66, 76, -70, 114, 100, 90, 123, -78, -119, -76, 36, 88, -123, -26, -30, -112, 76, -107, -53};
}
