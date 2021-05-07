package com.java.backend.controllers;

import com.java.backend.entities.Appointment;
import com.java.backend.repositories.AppointmentRepository;
import com.java.backend.repositories.CardRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    private final AppointmentRepository appointmentRepository;
    private final CardRepository cardRepository;

    public AdminController(AppointmentRepository appointmentRepository, CardRepository cardRepository)
    {
        this.appointmentRepository = appointmentRepository;
        this.cardRepository = cardRepository;
    }

    @PostMapping("/appointment")
    public Appointment createAppointment(@RequestBody Appointment newAppointment)
    {
        return appointmentRepository.save(newAppointment);
    }


}
