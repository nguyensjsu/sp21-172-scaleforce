package com.java.backend.controllers;

import com.java.backend.entities.Appointment;
import com.java.backend.entities.PatchUserAppointmentRequest;
import com.java.backend.repositories.AppointmentRepository;
import com.java.backend.repositories.CardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
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

    @GetMapping("/appointments")
    public List<Appointment> getOpenAppointments(@RequestParam(value = "startDate") Date startDate)
    {
        //TODO: test
        return appointmentRepository.findAppointmentByStartDate(startDate);
    }

    @PatchMapping("/appointment/{id}")
    public Appointment updateAppointment(@PathVariable Long id, PatchUserAppointmentRequest reqBody)
    {
        //TODO: test
        return appointmentRepository.findById(id).map(
                appointment -> {
                    appointment.setBarber(reqBody.getBarber());
                    return appointmentRepository.save(appointment);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Appointment with id \"%s\" not found", id)));
    }
}
