package com.java.backend.controllers;

import com.java.backend.entities.Appointment;
import com.java.backend.repositories.AppointmentRepository;
import com.java.backend.repositories.CardRepository;
import com.java.backend.requests.PatchUserAppointmentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@Secured({"ROLE_ADMIN", "ROLE_OFFICE", "ROLE_USER"})
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
    public List<Appointment> getOpenAppointments(@RequestParam(value = "startDate", required = false) String startDate,
                                                 @RequestParam(value = "endDate", required = false) String endDate)
    {
        if ((startDate == null && endDate != null) ||
                (startDate != null && endDate == null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must use both start and end date when filtering");

        // if no filters, return all unbooked records
        if (startDate == null && endDate == null)
            return appointmentRepository.findAppointmentsByBookedUserIdIsNull();
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

    @GetMapping("/appointments/{id}")
    public List<Appointment> getOpenAppointmentsById(@PathVariable String id)
    {
        return appointmentRepository.findAppointmentsByBookedUserId(id);
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
