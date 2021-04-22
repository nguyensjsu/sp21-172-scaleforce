package com.example.authserver.entities;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "HAIRCUT_USERS")
public class User
{
    @Id @GeneratedValue
    private Long id;

    @NonNull @Column(unique=true)
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
