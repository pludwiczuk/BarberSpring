package com.spring.barber.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.barber.appointment.Appointment;
import com.spring.barber.location.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private int price; // cents
    private int duration; // minutes

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonIgnoreProperties({"services", "timetables", "address", "business"})
    private Location location;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;
}
