package com.spring.barber.appointment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.barber.service.Service;
import com.spring.barber.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonIgnoreProperties("appointment")
    private Service service;

    private Status status;

    public Appointment(CreateAppointmentDto dto) {
        this.date = dto.getDate();
        this.user = new User();
        this.service = new Service();
        this.status = Status.CURRENT;
    }

}
