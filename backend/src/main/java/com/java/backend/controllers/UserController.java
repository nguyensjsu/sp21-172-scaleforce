package com.java.backend.controllers;

import com.java.backend.entities.Appointment;
import com.java.backend.repositories.AppointmentRepository;
import com.java.backend.repositories.CardRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final AppointmentRepository appointmentRepository;
    private final CardRepository cardRepository;

    public UserController(AppointmentRepository appointmentRepository, CardRepository cardRepository)
    {
        this.appointmentRepository = appointmentRepository;
        this.cardRepository = cardRepository;
    }

//    @GetMapping("/appointments/open")
//    public List<Appointment> getOpenAppointments()
//    {
//
//    }
//
//    @PatchMapping("/appointment/{id}")
//    public Appointment createAppointment(@RequestBody Appointment newAppointment)
//    {
//
//    }
}
