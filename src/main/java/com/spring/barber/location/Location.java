package com.spring.barber.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.barber.business.Business;
import com.spring.barber.address.Address;
import com.spring.barber.service.Service;
import com.spring.barber.timetable.Timetable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride( name = "line1", column = @Column(name = "address_line1")),
        @AttributeOverride( name = "line2", column = @Column(name = "address_line2")),
        @AttributeOverride( name = "postCode", column = @Column(name = "address_post_code")),
        @AttributeOverride( name = "city", column = @Column(name = "address_city")),
        @AttributeOverride( name = "country", column = @Column(name = "address_coutry"))
    })
    private Address address;

    @ManyToOne
    @JoinColumn(name = "business_id")
    @JsonIgnoreProperties("address")
    private Business business;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Service> services;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("location")
    private List<Timetable> timetables;

    public Location(CreateLocationDto dto) {
        this.name = dto.getName();
        this.address = new Address(dto.getAddress());
        this.business = new Business();
        this.services = new ArrayList<>();
    }

}
