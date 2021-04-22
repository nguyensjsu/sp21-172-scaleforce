package com.example.authserver.entities;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class User
{
    @Id @GeneratedValue
    private Long id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull @ElementCollection
    private List<String> permissions;

    public User()
    {
        permissions = new ArrayList<>();
    }

    public void addPermission(String permission)
    {
        permissions.add(permission);
    }
}
