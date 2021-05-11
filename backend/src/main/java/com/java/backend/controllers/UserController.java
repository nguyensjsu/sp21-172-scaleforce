package com.java.backend.controllers;

import com.java.backend.entities.Appointment;
import com.java.backend.bodies.PatchUserAppointmentRequest;
import com.java.backend.repositories.AppointmentRepository;
import com.java.backend.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final AppointmentRepository appointmentRepository;
    private final CardRepository cardRepository;
    private final SimpleDateFormat dateTimeFormat;

    public UserController(AppointmentRepository appointmentRepository, CardRepository cardRepository,
                          @Value("${spring.jackson.date-format}") SimpleDateFormat dateTimeFormat)
    {
        this.appointmentRepository = appointmentRepository;
        this.cardRepository = cardRepository;
        this.dateTimeFormat = dateTimeFormat;
    }

    @GetMapping("/appointments/open")
    public List<Appointment> getAllOpenAppointments()
    {
        return appointmentRepository.findAppointmentsByBookedUserIdIsNull();
    }

    @GetMapping("/appointments")
    public List<Appointment> getAppointmentsByRange(@RequestParam(value = "startDate") String startDate,
                                                    @RequestParam(value = "endDate") String endDate)
    {
        try
        {
            return appointmentRepository
                    .findAppointmentsByStartDateLessThanEqualAndEndDateGreaterThanEqualAndBookedUserIdIsNull(
                            dateTimeFormat.parse(startDate), dateTimeFormat.parse(endDate));
        } catch (ParseException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Date format is invalid, correct pattern is %s", dateTimeFormat.toPattern()));
        }

    }

    @PatchMapping("/appointment/{id}")
    public Appointment updateAppointment(@PathVariable Long id, @RequestBody PatchUserAppointmentRequest reqBody)
    {
        return appointmentRepository.findById(id).map(
                appointment -> {
                    appointment.setBookedUserId(reqBody.getUserId());
                    return appointmentRepository.save(appointment);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Appointment with id %s not found", id)));
    }
}
