package com.example.authserver.controllers;

import com.example.authserver.AuthProperties;
import com.example.authserver.entities.HaircutUser;
import com.example.authserver.entities.Permission;
import com.example.authserver.repositories.UserRepository;
import com.example.authserver.requests.AuthRequest;
import com.example.authserver.requests.NewUserRequest;
import com.example.authserver.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
class AuthController {

    private final UserRepository repository;
    private final AuthProperties authProperties;
    private final JwtUtil jwtUtil;

    AuthController(UserRepository repository, AuthProperties authProperties) {
        this.repository = repository;
        this.authProperties = authProperties;
        this.jwtUtil = new JwtUtil(authProperties, repository);
    }

    @PostMapping("/auth")
    Map<String, String> getJWT(@RequestBody AuthRequest authRequest)
    {
        HaircutUser haircutUser = repository.findByEmail(authRequest.getEmail());
        if (haircutUser == null || !authRequest.getPassword().equals(haircutUser.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        return Collections.singletonMap("jwt", jwtUtil.buildJws(haircutUser.getEmail(), haircutUser.getPermission()));
    }

    @PostMapping("/validate")
    Map<String, String> validateJWT(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth)
    {
        if (!jwtUtil.validateJwt(auth))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return Collections.singletonMap("jwt", "valid");
    }

    @GetMapping("/users")
    List<HaircutUser> allUsers() {
        return repository.findAll();
    }

    @PostMapping("/users")
    HaircutUser newUser(@RequestBody NewUserRequest newUser, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth)
    {
        Jws<Claims> claims = jwtUtil.getClaims(auth);
        Permission claimPermission = Permission.valueOf((String) claims.getBody().get("type"));

        if (!claimPermission.hasPermission(Permission.valueOf(newUser.getPermission())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Permission");
        return repository.save(new HaircutUser(newUser.getEmail(), newUser.getPassword(), Permission.USER));
    }

    // Single item
    @GetMapping("/user/{id}")
    HaircutUser getUser(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id \"%s\" not found", id)));
    }

    @PutMapping("/user/{id}")
    HaircutUser replaceUser(@PathVariable Long id, @RequestBody AuthRequest authRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth)
    {
        return repository.findById(id).map(
                haircutUser -> {
                    haircutUser.setEmail(authRequest.getEmail());
                    haircutUser.setPassword(authRequest.getPassword());
                    return repository.save(haircutUser);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id \"%s\" not found", id)));
    }

    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable Long id)
    {
        repository.deleteById(id);
    }

    private Jws<Claims> getClaimsFromHeader(String authHeader)
    {
        String jwt = authHeader.substring(7);
        return jwtUtil.getClaims(jwt);
    }
}
