package com.example.authserver.util;

import com.example.authserver.AuthProperties;
import com.example.authserver.entities.Permission;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil
{
    private final AuthProperties authProperties;

    private final JwtParser jwtParser;

    @Autowired
    public JwtUtil(AuthProperties properties)
    {
        this.authProperties = properties;
        jwtParser = Jwts.parserBuilder()
                // param here should be base64 encoded key
                .setSigningKey(authProperties.getKey())
                .build();
    }

    public boolean validateJwt(String jwt)
    {
        try {
//        throws these exceptions on failure:
//        ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException
        Jws<Claims> claims = jwtParser.parseClaimsJws(jwt);
        return true;
        }
        catch (Exception e) {
//            any exception means invalid, so return false
            return false;
        }
    }

    public Jws<Claims> getClaims(String jwt)
    {
        try {
            return jwtParser.parseClaimsJws(jwt);
        } catch (JwtException e) { return null; }
    }

    public String buildJws(String username, Permission privilege)
    {
        byte[] decodedKey = Base64.getDecoder().decode(authProperties.getKey());
        Key key = new SecretKeySpec(decodedKey, "HmacSHA256");

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(authProperties.getIssuer())
                .setSubject(username)
                .claim("type", privilege)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(authProperties.getWindow())))
                .signWith(key)
                .compact();
    }
}
