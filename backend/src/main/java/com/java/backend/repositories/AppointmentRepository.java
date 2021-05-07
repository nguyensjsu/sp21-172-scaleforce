package com.java.backend.repositories;

import com.java.backend.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>
{
        List<Appointment> findAppointmentByStartDate(Date startDate);
}
