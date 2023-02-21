package com.spring.barber.appointment;

import com.spring.barber.service.Service;
import com.spring.barber.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentResponse {

    private long id;
    private LocalDateTime date;
    private UserResponse user;
    private Service service;
    private Status status;

    public AppointmentResponse(Appointment appointment) {
        this.id = appointment.getId();
        this.date = appointment.getDate();
        this.user = new UserResponse(appointment.getUser());
        this.service = appointment.getService();
        this.status = appointment.getStatus();
    }

}
