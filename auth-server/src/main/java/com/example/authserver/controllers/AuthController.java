package com.example.authserver.controllers;

import com.example.authserver.AuthProperties;
import com.example.authserver.entities.Permission;
import com.example.authserver.entities.User;
import com.example.authserver.repositories.UserRepository;
import com.example.authserver.requests.AuthRequest;
import com.example.authserver.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;

@RestController
class AuthController {

    private final UserRepository repository;
    private final AuthProperties authProperties;

    AuthController(UserRepository repository, AuthProperties authProperties) {
        this.repository = repository;
        this.authProperties = authProperties;
    }

    @PostMapping("/auth")
    Map<String, String> getJWT(@RequestBody AuthRequest authRequest)
    {
        User user = repository.findByUsername(authRequest.getUsername());
        if (user == null || !authRequest.getPassword().equals(user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        return Collections.singletonMap("jwt", new JwtUtil(authProperties).buildJws(user.getUsername(), user.getPermission()));
    }

    @PostMapping("/validate")
    Map<String, String> validateJWT(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth)
    {
        String jwt = auth.substring(7);
        JwtUtil jwtUtil = new JwtUtil(authProperties);

        // validates the jwt
        if (!jwtUtil.validateJwt(jwt))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        Jws<Claims> claims = jwtUtil.getClaims(jwt);
        // issuer matches
        if (claims == null || !claims.getBody().getIssuer().equals(authProperties.getIssuer()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        // subject of token exists in db
        if (repository.findByUsername(claims.getBody().getSubject()) == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");


        return Collections.singletonMap("jwt", "valid");
    }

    @PostMapping("/users")
    User newUser(@RequestBody AuthRequest newUser) {
        return repository.save(new User(newUser.getUsername(), newUser.getPassword(), Permission.USER));
    }

    // Single item
    @GetMapping("/user/{id}")
    User getUser(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id \"%s\" not found", id)));
    }

    @PutMapping("/user/{id}")
    User replaceUser(@PathVariable Long id, @RequestBody AuthRequest authRequest)
    {
        return repository.findById(id).map(
                user -> {
                    user.setUsername(authRequest.getUsername());
                    user.setPassword(authRequest.getPassword());
                    return repository.save(user);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id \"%s\" not found", id)));
    }

    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable Long id)
    {
        repository.deleteById(id);
    }
}
