package com.java.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
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
    @DateTimeFormat
    private Date startDate;

    @NonNull
    @DateTimeFormat
    private Date endDate;

    @NonNull
    private String barber;

    @NonNull
    private HaircutService serviceId;

    private String booked;
}
