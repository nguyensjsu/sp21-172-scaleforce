package com.example.authserver.controllers;

import com.example.authserver.AuthProperties;
import com.example.authserver.entities.HaircutUser;
import com.example.authserver.entities.Role;
import com.example.authserver.repositories.UserRepository;
import com.example.authserver.requests.NewUserRequest;
import com.example.authserver.requests.PatchUserRequest;
import com.example.authserver.requests.UserRequest;
import com.example.authserver.responses.GetJwtResponse;
import com.example.authserver.responses.ValidateJwtResponse;
import com.example.authserver.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

@RestController
class AuthController {

    private final UserRepository repository;
    private final JwtUtil jwtUtil;

    AuthController(UserRepository repository, AuthProperties authProperties) {
        this.repository = repository;
        this.jwtUtil = new JwtUtil(authProperties, repository);
    }

    @PostMapping("/auth")
    GetJwtResponse getJWT(@RequestBody UserRequest userRequest)
    {
        HaircutUser haircutUser = repository.findByEmail(userRequest.getEmail());
        if (haircutUser == null || !userRequest.getPassword().equals(haircutUser.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        HashMap<String, String> response = new HashMap<>();

        return new GetJwtResponse(jwtUtil.buildJws(haircutUser.getEmail(), haircutUser.getRole()),
                haircutUser.getId().toString());
    }

    @PostMapping("/validate")
    ValidateJwtResponse validateJWT(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth)
    {
        if (!jwtUtil.validateJwt(auth))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return new ValidateJwtResponse();
    }

    @GetMapping("/users")
    @Secured({"ROLE_ADMIN", "ROLE_OFFICE"})
    List<HaircutUser> allUsers() {
        return repository.findAll();
    }

    @PostMapping("/users")
    HaircutUser newUser(@RequestBody NewUserRequest newUser, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String auth)
    {
        Role role = Role.ROLE_USER;
        if (auth != null)
        {
            Jws<Claims> claims = jwtUtil.getClaims(auth);
            // get permission from header if valid
            Role claimRole =
                    claims != null ?
                            Role.valueOf((String) claims.getBody().get("type")) :
                            Role.ROLE_USER;
            // if they attempt to create an account with more permissions
            if (!claimRole.hasPermission(Role.valueOf(newUser.getPermission())))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Permission");
            role = Role.valueOf(newUser.getPermission());
        }
        return repository.save(new HaircutUser(newUser.getEmail(), newUser.getPassword(), role));
    }

    // Single item
    @GetMapping("/user/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_OFFICE"})
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
    @Secured({"ROLE_ADMIN", "ROLE_OFFICE"})
    void deleteUser(@PathVariable Long id)
    {
        repository.deleteById(id);
    }
}
