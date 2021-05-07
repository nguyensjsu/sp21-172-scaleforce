package com.java.backend.controllers;

import com.java.backend.entities.Appointment;
import com.java.backend.repositories.AppointmentRepository;
import com.java.backend.repositories.CardRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/office")
public class OfficeController
{
    private final AppointmentRepository appointmentRepository;
    private final CardRepository cardRepository;

    public OfficeController(AppointmentRepository appointmentRepository, CardRepository cardRepository)
    {
        this.appointmentRepository = appointmentRepository;
        this.cardRepository = cardRepository;
    }

    @GetMapping("/appointments")
    public List<Appointment> getAll()
    {
        return appointmentRepository.findAll();
    }
}
