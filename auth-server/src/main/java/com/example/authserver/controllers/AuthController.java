package com.example.authserver.controllers;

import com.example.authserver.AuthProperties;
import com.example.authserver.entities.HaircutUser;
import com.example.authserver.entities.Permission;
import com.example.authserver.repositories.UserRepository;
import com.example.authserver.requests.NewUserRequest;
import com.example.authserver.requests.PatchUserRequest;
import com.example.authserver.requests.UserRequest;
import com.example.authserver.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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
    Map<String, String> getJWT(@RequestBody UserRequest userRequest)
    {
        HaircutUser haircutUser = repository.findByEmail(userRequest.getEmail());
        if (haircutUser == null || !userRequest.getPassword().equals(haircutUser.getPassword()))
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
    @Secured({"ADMIN", "OFFICE"})
    List<HaircutUser> allUsers() {
        return repository.findAll();
    }

    @PostMapping("/users")
    HaircutUser newUser(@RequestBody NewUserRequest newUser, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String auth)
    {
        Permission permission = Permission.USER;
        if (auth != null)
        {
            Jws<Claims> claims = jwtUtil.getClaims(auth);
            // get permission from header if valid
            Permission claimPermission =
                    claims != null ?
                            Permission.valueOf((String) claims.getBody().get("type")) :
                            Permission.USER;
            // if they attempt to create an account with more permissions
            if (!claimPermission.hasPermission(Permission.valueOf(newUser.getPermission())))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Permission");
            permission = Permission.valueOf(newUser.getPermission());
        }
        return repository.save(new HaircutUser(newUser.getEmail(), newUser.getPassword(), permission));
    }

    // Single item
    @GetMapping("/user/{id}")
    @Secured({"ADMIN", "OFFICE"})
    HaircutUser getUserById(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id \"%s\" not found", id)));
    }

    @PatchMapping("/user/{id}")
    HaircutUser UpdatePassword(@PathVariable Long id, @RequestBody PatchUserRequest userRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth)
    {
        Jws<Claims> claims = jwtUtil.getClaims(auth);

        // if token email doesn't match body email
        if (claims == null || !claims.getBody().getSubject().equals(userRequest.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");


        return repository.findById(id).map(
                haircutUser -> {
                    if (userRequest.getOldPassword().equals(haircutUser.getPassword()))
                        haircutUser.setPassword(userRequest.getNewPassword());
                    return repository.save(haircutUser);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id \"%s\" not found", id)));
    }

    @DeleteMapping("/user/{id}")
    @Secured({"ADMIN", "OFFICE"})
    void deleteUser(@PathVariable Long id)
    {
        repository.deleteById(id);
    }
}
