package com.spring.barber.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.barber.address.Address;

import com.spring.barber.businessUser.BusinessUser;
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
@Table(name = "businesses")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Location> locations;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BusinessUser> businessUsers;

    public Business(CreateBusinessDto dto) {
        this.name = dto.getName();
        this.address = new Address(dto.getAddress());
    }

}
