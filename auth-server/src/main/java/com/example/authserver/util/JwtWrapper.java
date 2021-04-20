package com.example.authserver.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class JwtWrapper
{
//    @Value("${jwt.window}")
    static int JWT_WINDOW = 120;

    @Value("${jwt.key}")
    static String key_str;

    public static String buildJws()
    {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder()
                .setIssuer("ME")
                .setSubject("For testing")
                .claim("name", "Username")
                .claim("Haircut", "User")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(JWT_WINDOW)))
                .signWith(key)
                .compact();

        return jws;
    }

    public static void main(String[] args)
    {
        System.out.println(buildJws());
    }
}
