package com.java.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Card
{
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String userId;

    @NonNull
    private int haircutCount;
}
