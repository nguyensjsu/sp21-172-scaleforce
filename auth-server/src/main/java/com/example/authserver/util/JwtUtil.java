package com.example.authserver.util;

import com.example.authserver.AuthProperties;
import com.example.authserver.entities.Permission;
import com.example.authserver.repositories.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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

    private final UserRepository userRepository;

    @Autowired
    public JwtUtil(AuthProperties properties, UserRepository userRepository)
    {
        this.authProperties = properties;
        this.jwtParser = Jwts.parserBuilder()
                // param here should be base64 encoded key
                .setSigningKey(authProperties.getKey())
                .build();
        this.userRepository = userRepository;
    }

    public boolean validateJwt(String authHeader)
    {
        try {
            Jws<Claims> claims = jwtParser.parseClaimsJws(authHeader.substring(7));

            // issuer is wrong
            if (claims == null || !claims.getBody().getIssuer().equals(authProperties.getIssuer()))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

            // subject of token exists in db
            if (userRepository.findByEmail(claims.getBody().getSubject()) == null)
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        return true;
        }
//        throws these exceptions on failure to parse correctly:
//        ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, NullPointerException
//        Any exception means invalid jwt, so return false
        catch (Exception e) {
            return false;
        }
    }

    public Jws<Claims> getClaims(String jws)
    {
        // Also validates the jws
        try {
            return jwtParser.parseClaimsJws(jws);
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
