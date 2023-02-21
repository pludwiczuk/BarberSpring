package com.spring.barber.location;


import com.spring.barber.address.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponse {
    private long id;
    private String name;
    private Address address;

    public LocationResponse(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.address = location.getAddress();
    }
}
