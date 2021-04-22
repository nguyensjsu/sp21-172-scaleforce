package com.example.authserver.util;

import com.example.authserver.AuthProperties;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtWrapper
{
    private final AuthProperties authProperties;

    @Autowired
    public JwtWrapper(AuthProperties properties)
    {
        this.authProperties = properties;
    }

    public String buildJws(String username, String privilege)
    {
        byte[] decodedKey = Base64.getDecoder().decode(authProperties.getKey());
        Key key = new SecretKeySpec(decodedKey, "HmacSHA256");

        return Jwts.builder()
//                .setId(UUID.randomUUID().toString())
                .setIssuer("HaircutAuthServer")
                .setSubject(username)
                .claim("type", privilege)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(authProperties.getWindow())))
                .signWith(key)
                .compact();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException
    {
        Key secretKey = KeyGenerator.getInstance("HmacSHA256").generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println(encodedKey);


    }
}
