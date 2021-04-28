package com.example.authserver.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "HAIRCUT_USERS")
public class User
{
    @Id @GeneratedValue
    private Long id;

    @NonNull @Column(unique=true)
    private String email;

    @NonNull
    private String password;

    @NonNull
    private Permission permission;
}


