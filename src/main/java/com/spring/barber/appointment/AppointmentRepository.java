package com.spring.barber.appointment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByUserId(Long userId);
    List<Appointment> findAllByDateBetween(LocalDateTime date1, LocalDateTime date2);
}
