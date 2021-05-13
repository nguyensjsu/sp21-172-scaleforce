package com.java.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Appointment
{
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
//    @Column(unique=true)
    private Date startDate;

    @NonNull
//    @Column(unique=true)
    private Date endDate;

    @NonNull
    private String barber;

    @NonNull
    private HaircutService service;

    private String bookedUserId;
}
