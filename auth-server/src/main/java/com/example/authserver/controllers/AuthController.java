package com.example.authserver.controllers;

import com.example.authserver.AuthProperties;
import com.example.authserver.entities.User;
import com.example.authserver.repositories.UserRepository;
import com.example.authserver.requests.AuthRequest;
import com.example.authserver.util.JwtWrapper;
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


        return Collections.singletonMap("jwt", new JwtWrapper(authProperties).buildJws(user.getUsername(), user.getPermissions()));
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item
    @GetMapping("/user/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cant find it! :)"));
    }

//    @PutMapping("/employees/{id}")
//    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//
//        return repository.findById(id)
//                .map(employee -> {
//                    employee.setName(newEmployee.getName());
//                    employee.setRole(newEmployee.getRole());
//                    return repository.save(employee);
//                })
//                .orElseGet(() -> {
//                    newEmployee.setId(id);
//                    return repository.save(newEmployee);
//                });
//    }

//    @DeleteMapping("/employees/{id}")
//    void deleteEmployee(@PathVariable Long id) {
//        repository.deleteById(id);
//    }
}
