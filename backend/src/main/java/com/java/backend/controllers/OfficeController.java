package com.java.backend.controllers;

import com.java.backend.entities.Appointment;
import com.java.backend.entities.Card;
import com.java.backend.repositories.AppointmentRepository;
import com.java.backend.repositories.CardRepository;
import com.java.backend.requests.CardPatchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@Secured({"ROLE_ADMIN", "ROLE_OFFICE"})
@RequestMapping("/office")
public class OfficeController
{
    private final AppointmentRepository appointmentRepository;
    private final CardRepository cardRepository;
    private final SimpleDateFormat dateTimeFormat;

    public OfficeController(AppointmentRepository appointmentRepository, CardRepository cardRepository,
                            @Value("${spring.jackson.date-format}") SimpleDateFormat dateTimeFormat)
    {
        this.appointmentRepository = appointmentRepository;
        this.cardRepository = cardRepository;
        this.dateTimeFormat = dateTimeFormat;
    }

    @GetMapping("/appointments")
    public List<Appointment> getAppointmentsByRange(@RequestParam(value = "startDate", required = false) String startDate,
                                                    @RequestParam(value = "endDate", required = false) String endDate)
    {
        // if the filters are in an invalid configuration
        if ((startDate == null && endDate != null) ||
                (startDate != null && endDate == null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must use both start and end date when filtering");

        // if no filters, return all records
        if (startDate == null && endDate == null)
            return appointmentRepository.findAll();
        try
        {
            return appointmentRepository
                    .findAppointmentsByStartDateLessThanEqualAndEndDateGreaterThanEqual(
                            dateTimeFormat.parse(startDate), dateTimeFormat.parse(endDate));
        } catch (ParseException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Date format is invalid, correct pattern is %s", dateTimeFormat.toPattern()));
        }

    }

    @GetMapping("/cards")
    public List<Card> getAllCards()
    {
        return cardRepository.findAll();
    }

    @GetMapping("/card/{id}")
    public Card getCard(@PathVariable Long id)
    {
        return cardRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Card with id %s not found", id)));
    }


    @PatchMapping("/card/{id}")
    public Card updateCard(@PathVariable Long id, @RequestBody CardPatchRequest input)
    {
        return cardRepository.findById(id).map(
                card -> {
                    card.setHaircutCount(input.getHaircutCount());
                    return cardRepository.save(card);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Card with id %s not found", id)));
    }

    @DeleteMapping("/card/{id}")
    public void deleteCard(@PathVariable Long id)
    {
        cardRepository.deleteById(id);
    }
}
