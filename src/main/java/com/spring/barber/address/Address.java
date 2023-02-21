package com.spring.barber.address;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Address {

    private String line1;
    private String line2;
    private String postCode;
    private String city;
    private String country;

    public Address(AddressDto dto) {
        this.line1 = dto.getLine1();
        this.line2 = dto.getLine2();
        this.postCode = dto.getPostCode();
        this.city = dto.getCity();
        this.country = dto.getCountry();
    }

}
