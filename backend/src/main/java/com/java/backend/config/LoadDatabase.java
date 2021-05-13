package com.java.backend.config;

import com.java.backend.entities.Appointment;
import com.java.backend.entities.HaircutService;
import com.java.backend.repositories.AppointmentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private final SimpleDateFormat dateTimeFormat;

    public LoadDatabase(@Value("${spring.jackson.date-format}") String dateTimeFormat)
    {
        this.dateTimeFormat = new SimpleDateFormat(dateTimeFormat);
    }

    @Bean
    CommandLineRunner initDatabase(AppointmentRepository repository) throws ParseException
    {
        Date date = dateTimeFormat.parse("2021-10-10 12:30:00 +0000");
        Date date2 = dateTimeFormat.parse("2021-10-10 13:30:00 +0000");
        Date date3 = dateTimeFormat.parse("2021-10-10 14:30:00 +0000");
        Date date4 = dateTimeFormat.parse("2021-10-10 15:30:00 +0000");
//        Date date5 = dateTimeFormat.parse("2021-10-10 16:30:00");
//        Date date6 = dateTimeFormat.parse("2021-10-10 17:30:00");
        Appointment a = new Appointment(date2, date3, "CoolBarber", HaircutService.CUT_AND_BEARD);
        a.setBookedUserId("50");

        return args -> {
            log.info("Preloading " + repository.save(new Appointment(date, date2, "Barber1", HaircutService.TRIM)));
            log.info("Preloading " + repository.save(a));
            log.info("Preloading " + repository.save(new Appointment(date3, date4, "AnotherBarber", HaircutService.SHAVE)));
        };
    }
}
