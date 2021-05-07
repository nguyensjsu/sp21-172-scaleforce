package com.java.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
    @Column(unique=true)
    @DateTimeFormat
    private String startDate;

    @NonNull
    @Column(unique=true)
    @DateTimeFormat
    private String endDate;

    @NonNull
    private String barber;

    @NonNull
    private HaircutService service;

    private String booked;
}
